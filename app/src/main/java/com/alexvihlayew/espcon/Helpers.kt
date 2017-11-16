package com.alexvihlayew.espcon

/**
 * Created by alexvihlayew on 16/11/2017.
 */

inline fun <T> T.let(fulfill: (T) -> Unit, reject: () -> Unit) = if (this != null) { fulfill(this) } else { reject() }