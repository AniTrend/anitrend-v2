/*
 * Copyright (C) 2019  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.core.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import co.anitrend.arch.extension.ext.isLowRamDevice

@GlideModule
class GlideAppModule: AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val isLowRamDevice = context.isLowRamDevice()

        val calculator = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens((if (isLowRamDevice) 1 else 2).toFloat()).build()

        // Increasing cache & pool by 25% - default is 250MB
        val memoryCacheSize = (1.25 * calculator.memoryCacheSize).toInt()
        val bitmapPoolSize = (1.25 * calculator.bitmapPoolSize).toInt()
        var storageCacheSize = 1024 * 1024 * 350

        if (context.externalCacheDir != null) {
            val total = context.externalCacheDir!!.totalSpace
            storageCacheSize = (total * 0.2).toInt()
        }

        builder.setMemoryCache(LruResourceCache(memoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSize.toLong()))
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, storageCacheSize.toLong()))

        // Setting default params for glide
        val options = RequestOptions()
            .format(if (isLowRamDevice) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888)
            .timeout(GLIDE_TIME_OUT)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            /*.error(context.getCompatDrawable(R.drawable.empty_icon))*/

        builder.setDefaultRequestOptions(options)
    }

    companion object {
        /** timeout for the http requests used to load the image */
        const val GLIDE_TIME_OUT = 10000
    }
}