package ms.pedido;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MsPedidoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPedidoApplication.class, args);
    }

}
