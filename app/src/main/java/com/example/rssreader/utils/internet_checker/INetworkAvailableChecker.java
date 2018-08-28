package com.example.rssreader.utils.internet_checker;

import com.example.rssreader.utils.fx.core.Flow;

public interface INetworkAvailableChecker {

    Flow<Boolean> checkNetworkAvailable();

}
