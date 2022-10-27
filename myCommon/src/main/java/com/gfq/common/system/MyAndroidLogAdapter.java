package com.gfq.common.system;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gfq.common.BuildConfig;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Android terminal log output implementation for {@link LogAdapter}.
 *
 * Prints output to LogCat with pretty borders.
 *
 * <pre>
 *  ┌──────────────────────────
 *  │ Method stack history
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Log message
 *  └──────────────────────────
 * </pre>
 */
public class MyAndroidLogAdapter implements LogAdapter {

  private boolean isLoggable = true;
  @NonNull
  private final FormatStrategy formatStrategy;

  public MyAndroidLogAdapter() {
    this.formatStrategy = PrettyFormatStrategy.newBuilder().build();
  }

  public MyAndroidLogAdapter(@NonNull FormatStrategy formatStrategy) {
    this.formatStrategy = formatStrategy;
  }

  @Override public boolean isLoggable(int priority, @Nullable String tag) {
    return isLoggable;
  }

  @Override public void log(int priority, @Nullable String tag, @NonNull String message) {
    formatStrategy.log(priority, tag, message);
  }

  public void isLoggable(boolean boo) {
    this.isLoggable = boo;
  }
}
