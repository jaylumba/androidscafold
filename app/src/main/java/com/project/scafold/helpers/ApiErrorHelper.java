package com.project.scafold.helpers;


import com.project.scafold.models.others.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by jayan on 11/5/2016.
 */

public class ApiErrorHelper {

    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter =
                AppConstants.RETROFIT.responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }
        return error;
    }
}
