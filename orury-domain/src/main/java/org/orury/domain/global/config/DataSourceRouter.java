package org.orury.domain.global.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DataSourceRouter extends AbstractRoutingDataSource {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected Object determineCurrentLookupKey() {
        // @Transactionl(readOnly = true) 이면 True 이다.
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (readOnly) {
            log.info("readOnly = true, request to replica");
        }
        log.info("readOnly = false, request to source");
        return readOnly ? "read" : "write";
    }
}
