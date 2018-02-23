package net.snorp.offscreenviews;

import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;

public class OffscreenViewRenderer {
    private Context mContext;
    private VirtualDisplay mVirtualDisplay;
    private Surface mSurface;
    private OffscreenViewPresentation mPresentation;

    private DisplayMetrics mDefaultMetrics;

    public OffscreenViewRenderer(Context context, Surface surface, int width, int height) {
        mContext = context;

        DisplayManager manager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display defaultDisplay = manager.getDisplay(Display.DEFAULT_DISPLAY);

        mDefaultMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(mDefaultMetrics);

        int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY |
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION |
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

        mVirtualDisplay = manager.createVirtualDisplay("OffscreenViews", width, height, mDefaultMetrics.densityDpi, surface, flags);

        mPresentation = new OffscreenViewPresentation(mContext, mVirtualDisplay.getDisplay());
        mPresentation.show();
    }

    public void setView(View view) {
        if (mPresentation == null) {
            throw new IllegalStateException("No presentation!");
        }

        mPresentation.setView(view);
    }

    public void setView(int resourceId) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setView(inflater.inflate(resourceId, null));
    }

    public void resize(int width, int height) {
        if (mVirtualDisplay == null) {
            throw new IllegalStateException("No virtual display!");
        }

        mVirtualDisplay.resize(width, height, mDefaultMetrics.densityDpi);
    }

    public void release() {
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
        }

        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }

        if (mSurface != null) {
            mSurface.release();
        }
    }

    class OffscreenViewPresentation extends Presentation {
        public OffscreenViewPresentation(Context context, Display display) {
            super(context, display);
        }

        public void setView(View view) {
            setContentView(view);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }
}
