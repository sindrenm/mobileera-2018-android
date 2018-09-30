package rocks.mobileera.mobileera.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.left = 0
        outRect?.right = 40
        outRect?.top = 0
        outRect?.bottom = 40
    }


}