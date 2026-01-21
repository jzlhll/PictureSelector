package com.luck.picture.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.luck.picture.lib.R;

/**
 * @author：luck
 * @date：2021/11/17 10:45 上午
 * @describe：TitleBar
 */
public class TitleSelectorBar extends TitleBar {

    private ImageView selectAllImage;

    private boolean mIsAllSelected = false;

    private OnSelectAllListener mOnSelectAllListener;

    public TitleSelectorBar(Context context) {
        super(context);
    }

    public TitleSelectorBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleSelectorBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getLayoutId() {
        return R.layout.ps_title_bar_selector;
    }

    /**
     * 不论哪个被调用，你需要调用[setCurrentNum]来刷新最新状态。
     */
    public interface OnSelectAllListener {
        void onSelectAll();
        void onCancelSelectAll();
    }

    /**
     * 设置了这个全选，就激活了全选模式，取消了cancel按钮。
     */
    public void setOnSelectAllListener(@NonNull OnSelectAllListener listener) {
        mOnSelectAllListener = listener;
        selectAllImage = findViewById(R.id.ps_select_all);
        selectAllImage.setVisibility(View.VISIBLE);
        selectAllImage.setOnClickListener((OnClickListener) v -> {
            if (mIsAllSelected) {
                mOnSelectAllListener.onCancelSelectAll();
            } else {
                mOnSelectAllListener.onSelectAll();
            }
        });
    }

    public void resetToNoneSelect(String from) {
        if (mOnSelectAllListener == null) {
            return;
        }
        mIsAllSelected = false;
//            selectAllImage.contentDescription = context.getString(R.string.select_all)
        selectAllImage.setImageResource(R.drawable.ps_select_all);
    }

    public void setSelectedChange(int currentSelectCount) {
        if (mOnSelectAllListener == null) {
            return;
        }

        if (mIsAllSelected) {
            if (currentSelectCount == 0) {
                mIsAllSelected = false;
//            selectAllImage.contentDescription = context.getString(R.string.select_all)
                selectAllImage.setImageResource(R.drawable.ps_select_all);
            }
        } else {
            if (currentSelectCount > 0) {
                mIsAllSelected = true;
                selectAllImage.setImageResource(R.drawable.ps_select_cancel);
            }
        }
    }
}
