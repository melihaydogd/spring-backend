package com.project.backend;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

    private static final String IMAGE_VERSION = "postgres:15.3-alpine";
    private static PostgresContainer container;

    private PostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresContainer getInstance() {
        if (container == null) {
            container = new PostgresContainer();
            container.start();
            container.withInitScript("initTestDatabase.sql");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    public void runInitScript() {
        container.runInitScriptIfRequired();
    }

}
