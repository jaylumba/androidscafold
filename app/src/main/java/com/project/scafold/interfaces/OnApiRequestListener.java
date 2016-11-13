package com.project.scafold.interfaces;

/**
 * Created by jayan on 11/5/2016.
 */
public interface OnApiRequestListener {

    void onApiRequestBegin(final String action);
    void onApiRequestSuccess(final String action, final Object result);
    void onApiRequestFailed(final String action, final Throwable t);
}
