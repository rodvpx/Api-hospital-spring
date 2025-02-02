package rodvpx.com.github.apihospitalspring.util;

import reactor.core.publisher.Mono;
import com.google.api.core.ApiFuture;

import java.util.concurrent.Executors;

public class ApiFutureUtils {

    // Método genérico para converter ApiFuture para Mono
    public static <T> Mono<T> fromApiFuture(ApiFuture<T> future) {
        return Mono.create(sink ->
                future.addListener(() -> {
                    try {
                        sink.success(future.get());
                    } catch (Exception e) {
                        sink.error(e);
                    }
                }, Executors.newSingleThreadExecutor())
        );
    }
}
