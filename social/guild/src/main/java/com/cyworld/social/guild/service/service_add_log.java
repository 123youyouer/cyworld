package com.cyworld.social.guild.service;

import com.cyworld.social.guild.utils.data_service_manager;
import com.cyworld.social.utils.request.guild.req_add_log;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Component
public class service_add_log {
    @Builder
    public static class context {
        req_add_log req;
        data_service_manager.data_service_instance guild_instance;
    }

    @Autowired
    private data_service_manager dataServiceManager;

    public Mono serv(ServerRequest request){
        context this_conext = context
                .builder()
                .guild_instance(dataServiceManager.cached_instance.getIfPresent("guild"))
                .build();
        return request.bodyToMono(req_add_log.class)
                .doOnNext(req->{
                    this_conext.req=req;
                })
                .flatMap(req->{
                    return WebClient.create()
                            .post()
                            .uri("http://" + this_conext.guild_instance.host + "/guild/v1/add_log")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromObject(this_conext.req))
                            .exchange()
                            .flatMap(res->{
                                switch (res.statusCode()){
                                    case OK:
                                        return res.bodyToMono(String.class)
                                                .flatMap(str->{
                                                    return ServerResponse.ok()
                                                            .body(BodyInserters.fromObject(str));
                                                });
                                    default:
                                        return res.bodyToMono(String.class).flatMap(str->Mono.error(new Throwable(str)));
                                }
                            });
                });
    }
}
