/*
 * Copyright (C) 2021 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tohandesign.spendingtrackingapp.purchase;

import android.app.Activity;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;

/**
 * The repository uses data from the Billing data source and the game state model together to give a
 * unified version of the state of the game to the ViewModel. It works closely with the
 * BillingDataSource to implement consumable items, premium items, etc.
 */
public class TrivialDriveRepository {

    final BillingDataSource billingDataSource;

    public TrivialDriveRepository(BillingDataSource billingDataSource) {
        this.billingDataSource = billingDataSource;
    }

    public void buySku(Activity activity, String sku) {
        billingDataSource.launchBillingFlow(activity, sku);
    }

    /**
     * Return LiveData that indicates whether the sku is currently purchased.
     *
     * @param sku the SKU to get and observe the value for
     * @return LiveData that returns true if the sku is purchased.
     */
    public LiveData<Boolean> isPurchased(String sku) {
        return billingDataSource.isPurchased(sku);
    }


    public final void refreshPurchases() {
        billingDataSource.refreshPurchasesAsync();
    }

    public final LifecycleObserver getBillingLifecycleObserver() {
        return billingDataSource;
    }

    // There's lots of information in SkuDetails, but our app only needs a few things, since our
    // goods never go on sale, have introductory pricing, etc.
    public final String getSkuTitle(String sku) {
        return billingDataSource.getSkuTitle(sku);
    }

    public final String getSkuPrice(String sku) {
        return billingDataSource.getSkuPrice(sku);
    }

    public final String getSkuDescription(String sku) {
        return billingDataSource.getSkuDescription(sku);
    }

    public final LiveData<Boolean> getBillingFlowInProcess() {
        return billingDataSource.getBillingFlowInProcess();
    }
}
