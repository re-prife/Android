package com.mirim.refrigerator.view.refrigeratorFragment

import android.content.AttributionSource
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.GridView

class ExpandableHeightGridView : GridView {
    var expanded = false;

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun isExpanded() : Boolean
    {
        return expanded;
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded())
        {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            var expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
            MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            val params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    fun setExpand(expanded: Boolean)
    {
        this.expanded = expanded;
    }
}