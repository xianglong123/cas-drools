package com.cas.config;

import org.kie.api.event.kiescanner.KieScannerEventListener;
import org.kie.api.event.kiescanner.KieScannerStatusChangeEvent;
import org.kie.api.event.kiescanner.KieScannerUpdateResultsEvent;

/**
 * @description:
 * @author: xianglong
 * @create: 2024-01-08 17:51
 **/

public class KieScennerListener implements KieScannerEventListener {

    @Override
    public void onKieScannerStatusChangeEvent(KieScannerStatusChangeEvent statusChange) {
        System.out.println("aaaaa");
    }

    @Override
    public void onKieScannerUpdateResultsEvent(KieScannerUpdateResultsEvent updateResults) {
        System.out.println("bbbbb");
    }
}
