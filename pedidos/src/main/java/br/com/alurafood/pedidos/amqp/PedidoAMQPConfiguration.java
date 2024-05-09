package br.com.alurafood.pedidos.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoAMQPConfiguration {

    // CRIA UM OBJETO DE CONVERSÃO NO MOMENTO DO ENVIO DAS MENSAGENS
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ALTERA O OBJETO DE CONVERSÃO DAS MENSAGENS
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }

    // CRIA A FILA (O CONSUMIDOR QUEM CRIA A FILA)
    @Bean
    public Queue filaDetalhesPedido() {
        return QueueBuilder.nonDurable("pagamentos.detalhes-pedido").build();
    }

    // CRIA A EXCHANGE COM O MESMO TIPO E NOME DA CRIADA NO APLICAÇÃO PRODUTORA
    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange("pagamentos.ex").build();
    }

    // FAZ O BINGING (LIGAÇÃO) ENTRE A QUEUE (FILA) E A EXCHANGE
    @Bean
    public Binding bindPagamentoPedido(FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(filaDetalhesPedido()).to(fanoutExchange());
    }

    // SEM O BEAN DE RABBITADMIN NÃO É POSSÍVEL CRIAR OBJETOS NO RABBITMQ
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}
