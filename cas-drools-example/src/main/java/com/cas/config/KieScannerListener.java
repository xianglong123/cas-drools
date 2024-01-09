package com.cas.config;

import org.kie.api.event.kiescanner.KieScannerEventListener;
import org.kie.api.event.kiescanner.KieScannerStatusChangeEvent;
import org.kie.api.event.kiescanner.KieScannerUpdateResultsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: xianglong
 * @create: 2024-01-08 17:51
 **/

public class KieScannerListener implements KieScannerEventListener {

    private static final Logger log = LoggerFactory.getLogger(KieScannerListener.class);

    @Override
    public void onKieScannerStatusChangeEvent(KieScannerStatusChangeEvent statusChange) {
        log.info("time=[{}], type=[{}], status={}[]", LocalDateTime.now(), "A", statusChange.getStatus());
    }

    @Override
    public void onKieScannerUpdateResultsEvent(KieScannerUpdateResultsEvent updateResults) {
        log.info("time=[{}], type=[{}], status={}[]", LocalDateTime.now(), "B", updateResults.getResults());
    }
}
