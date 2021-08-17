package ms.pedido;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableEurekaClient
public class MsPedidoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPedidoApplication.class, args);
    }

}
