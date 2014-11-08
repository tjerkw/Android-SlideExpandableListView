package com.tjerkw.slideexpandable.library;


import android.view.View;

public interface SlideExpandableListener {
    public void onStartExpandAnimation(View view, int position);
    public void onEndExpandAnimation(View view, int position);
    public void onStartCollapseAnimation(View view, int position);
    public void onEndCollapseAnimation(View view, int position);
}
