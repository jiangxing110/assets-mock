package com.qbit.assets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.qbit.assets.**.mapper")
@SpringBootApplication
public class AssetsMockApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetsMockApplication.class, args);
    }

}
