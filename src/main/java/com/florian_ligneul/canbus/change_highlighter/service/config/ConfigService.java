package com.florian_ligneul.canbus.change_highlighter.service.config;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Configuration service
 */
public class ConfigService {
    private Subject<EMessageDataFormat> dataFormatterObs = BehaviorSubject.create();

    /**
     * @return {@link Observable} of the currently used data formatter
     */
    public Observable<EMessageDataFormat> getDataFormatter() {
        return dataFormatterObs;
    }

    /**
     * Set a new data formatter
     *
     * @param dataFormatter the new data formatter
     */
    public void setDataFormatter(EMessageDataFormat dataFormatter) {
        dataFormatterObs.onNext(dataFormatter);
    }

    /**
     * @return the default data formatter
     */
    public EMessageDataFormat getDefaultFormatter() {
        return EMessageDataFormat.HEXADECIMAL;
    }
}
