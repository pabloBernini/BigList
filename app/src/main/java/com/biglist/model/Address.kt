package com.biglist.model

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("street") var street: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("suite") var suite: String? = null,
    @SerializedName("zipcode") var zipcode: String? = null,
    @SerializedName("geo") var geo: Geo? = null,


)