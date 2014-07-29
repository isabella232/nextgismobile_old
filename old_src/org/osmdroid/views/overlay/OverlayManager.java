package org.osmdroid.views.overlay;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay.Snappable;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class OverlayManager extends AbstractList<Overlay> {

	private boolean mUseSafeCanvas = true;

	private final CopyOnWriteArrayList<Overlay> mOverlayList;

	public OverlayManager() {
		mOverlayList = new CopyOnWriteArrayList<Overlay>();
	}

	@Override
	public Overlay get(final int pIndex) {
		return mOverlayList.get(pIndex);
	}

	@Override
	public int size() {
		return mOverlayList.size();
	}

	@Override
	public void add(final int pIndex, final Overlay pElement) {
		mOverlayList.add(pIndex, pElement);
		if (pElement instanceof SafeDrawOverlay)
			((SafeDrawOverlay) pElement).setUseSafeCanvas(this.isUsingSafeCanvas());
	}

	@Override
	public Overlay remove(final int pIndex) {
		return mOverlayList.remove(pIndex);
	}

	@Override
	public Overlay set(final int pIndex, final Overlay pElement) {
		Overlay overlay = mOverlayList.set(pIndex, pElement);
		if (pElement instanceof SafeDrawOverlay)
			((SafeDrawOverlay) pElement).setUseSafeCanvas(this.isUsingSafeCanvas());
		return overlay;
	}

	public boolean isUsingSafeCanvas() {
		return mUseSafeCanvas;
	}

	public void setUseSafeCanvas(boolean useSafeCanvas) {
		mUseSafeCanvas = useSafeCanvas;
		for (Overlay overlay : mOverlayList)
			if (overlay instanceof SafeDrawOverlay)
				((SafeDrawOverlay) overlay).setUseSafeCanvas(this.isUsingSafeCanvas());
	}

	public Iterable<Overlay> overlaysReversed() {
		return new Iterable<Overlay>() {
			@Override
			public Iterator<Overlay> iterator() {
				final ListIterator<Overlay> i = mOverlayList.listIterator(mOverlayList.size());

				return new Iterator<Overlay>() {
					@Override
					public boolean hasNext() {
						return i.hasPrevious();
					}

					@Override
					public Overlay next() {
						return i.previous();
					}

					@Override
					public void remove() {
						i.remove();
					}
				};
			}
		};
	}

	public void draw(final Canvas c, final MapView pMapView) {
		for (final Overlay overlay : mOverlayList) {
			if (overlay.isEnabled() && overlay.isDrawingShadowLayer()) {
				overlay.onDraw(c, pMapView, true);
			}
		}

		for (final Overlay overlay : mOverlayList) {
			if (overlay.isEnabled()) {
				overlay.onDraw(c, pMapView, false);
			}
		}

	}

	public void onDetach(final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			overlay.onDetach(pMapView);
		}
	}

	public boolean onKeyDown(final int keyCode, final KeyEvent event, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onKeyDown(keyCode, event, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onKeyUp(final int keyCode, final KeyEvent event, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onKeyUp(keyCode, event, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onTouchEvent(final MotionEvent event, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onTouchEvent(event, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onTrackballEvent(final MotionEvent event, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onTrackballEvent(event, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onSnapToItem(final int x, final int y, final Point snapPoint, final IMapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay instanceof Snappable) {
				if (((Snappable) overlay).onSnapToItem(x, y, snapPoint, pMapView)) {
					return true;
				}
			}
		}

		return false;
	}

	/* GestureDetector.OnDoubleTapListener */

	public boolean onDoubleTap(final MotionEvent e, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onDoubleTap(e, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onDoubleTapEvent(final MotionEvent e, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onDoubleTapEvent(e, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onSingleTapConfirmed(final MotionEvent e, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onSingleTapConfirmed(e, pMapView)) {
				return true;
			}
		}

		return false;
	}

	/* OnGestureListener */

	public boolean onDown(final MotionEvent pEvent, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onDown(pEvent, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onFling(final MotionEvent pEvent1, final MotionEvent pEvent2,
						   final float pVelocityX, final float pVelocityY, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onFling(pEvent1, pEvent2, pVelocityX, pVelocityY, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onLongPress(final MotionEvent pEvent, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onLongPress(pEvent, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public boolean onScroll(final MotionEvent pEvent1, final MotionEvent pEvent2,
							final float pDistanceX, final float pDistanceY, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onScroll(pEvent1, pEvent2, pDistanceX, pDistanceY, pMapView)) {
				return true;
			}
		}

		return false;
	}

	public void onShowPress(final MotionEvent pEvent, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			overlay.onShowPress(pEvent, pMapView);
		}
	}

	public boolean onSingleTapUp(final MotionEvent pEvent, final MapView pMapView) {
		for (final Overlay overlay : this.overlaysReversed()) {
			if (overlay.onSingleTapUp(pEvent, pMapView)) {
				return true;
			}
		}

		return false;
	}
}