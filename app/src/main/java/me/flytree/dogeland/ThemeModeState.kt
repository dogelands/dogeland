package me.flytree.dogeland

import android.Manifest
import android.app.Activity
import android.app.UiModeManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.PermissionChecker
import com.omarea.common.ui.ThemeMode

object ThemeModeState {
    private var themeMode: ThemeMode = ThemeMode()
    private fun checkPermission(context: Context, permission: String): Boolean = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED

    fun switchTheme(activity: Activity? = null): ThemeMode {
        if (activity != null) {
            val uiModeManager = activity.applicationContext.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            val nightMode = (uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES)

            // 设置壁纸作为背景需要读取外置存储权限
            if (ThemeConfig(activity).getAllowTransparentUI() && checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                val wallpaper = WallpaperManager.getInstance(activity)
                val wallpaperInfo = wallpaper.wallpaperInfo
                // 动态壁纸
                if (wallpaperInfo != null && wallpaperInfo.packageName != null) {
                }
                if (nightMode) {
                    themeMode.isDarkMode = true
                    activity.setTheme(R.style.AppThemeWallpaper)
                } else {
                    themeMode.isDarkMode = false
                    activity.setTheme(R.style.AppThemeWallpaperLight)
                }
                // activity.window.setBackgroundDrawable(activity.getDrawable(R.drawable.window_transparent));
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER)
            } else {
                if (nightMode) {
                    themeMode.isDarkMode = true
                    themeMode.isLightStatusBar = false
                    activity.setTheme(R.style.AppThemeDark)
                } else {
                    themeMode.isDarkMode = false
                }

                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //getWindow().setNavigationBarColor(Color.WHITE);
            }
            if (!themeMode.isDarkMode) {
                themeMode.isLightStatusBar = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

                activity.window.run {
                    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

                    // 设置白色状态栏和白色导航栏
                    // statusBarColor = Color.WHITE
                    // navigationBarColor = Color.WHITE

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    } else {
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }

        }
        return themeMode
    }

    fun getThemeMode(): ThemeMode {
        return themeMode
    }
}
