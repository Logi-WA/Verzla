package is.hi.verzla_backend.config;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import jakarta.annotation.PreDestroy;

/**
 * Connection manager for the Verzla backend database connections.
 *
 * <p>This component manages the lifecycle of database connections, ensuring that
 * connections are properly released when the application is shutting down. This
 * helps prevent connection leaks and ensures clean application termination.</p>
 *
 * <p>The manager works by:
 * <ul>
 *   <li>Being injected with the application's DataSource</li>
 *   <li>Registering a shutdown hook via {@code @PreDestroy}</li>
 *   <li>Closing active connections during application shutdown</li>
 * </ul>
 * </p>
 *
 * <p>Proper connection management is especially important in cloud environments
 * and containerized deployments where resources need to be released promptly.</p>
 *
 * @see javax.sql.DataSource
 * @see jakarta.annotation.PreDestroy
 */
@Component
public class ConnectionManager {

    /**
     * The application's primary data source that provides database connections
     */
    private final DataSource dataSource;

    /**
     * Constructs a new ConnectionManager with the specified data source.
     *
     * <p>Spring automatically injects the application's configured DataSource
     * into this constructor when creating the ConnectionManager bean.</p>
     *
     * @param dataSource The application's DataSource bean
     */
    public ConnectionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Closes active database connections when the application is shutting down.
     *
     * <p>This method is automatically called during application shutdown due to
     * the {@code @PreDestroy} annotation. It attempts to close any active
     * connections obtained from the data source to prevent connection leaks.</p>
     *
     * <p>If an error occurs during connection closing, the exception is caught
     * and logged, allowing the shutdown process to continue.</p>
     */
    @PreDestroy
    public void closeConnection() {
        try {
            if (dataSource != null) {
                dataSource.getConnection().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
