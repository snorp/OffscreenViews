package net.snorp.offscreenviews;

import android.graphics.SurfaceTexture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

public class MainActivity extends AppCompatActivity {
    private static final String LOGTAG = "MainActivity";

    private TextureView mTextureView;
    private OffscreenViewRenderer mViewRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We're going to display the contents of a SurfaceTexture
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                if (mViewRenderer == null) {
                    // We have a SurfaceTexture now, create the offscreen renderer and hand it a Surface that is
                    // connected to the SurfaceTexture that the TextureView is displaying
                    mViewRenderer = new OffscreenViewRenderer(MainActivity.this, new Surface(surfaceTexture), width, height);
                    mViewRenderer.setView(R.layout.offscreen_main);
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
                mViewRenderer.resize(width, height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                // Shut the renderer down
                if (mViewRenderer != null) {
                    mViewRenderer.release();
                    mViewRenderer = null;

                    // OffscreenViewRenderer releases the surface
                    return false;
                }

                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                // We don't care about this
            }
        });

        setContentView(mTextureView);
    }
}
