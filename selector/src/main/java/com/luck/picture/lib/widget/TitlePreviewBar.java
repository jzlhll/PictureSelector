package com.luck.picture.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.R;
import com.luck.picture.lib.style.TitleBarStyle;
import com.luck.picture.lib.utils.StyleUtils;

/**
 * @author：luck
 * @date：2021/11/19 4:38 下午
 * @describe：PreviewTitleBar
 */
public class TitlePreviewBar extends TitleBar {

    public TitlePreviewBar(Context context) {
        super(context);
    }

    public TitlePreviewBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitlePreviewBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextView tvSelected;

    public TextView tvSelectedWord;

    private Runnable selectedClick;

    public void setOnSelectClick(Runnable runnable) {
        selectedClick = runnable;
    }

    @Override
    int getLayoutId() {
        return R.layout.ps_title_bar_preview;
    }

    @Override
    public void setTitleBarStyle() {
        super.setTitleBarStyle();
        TitleBarStyle titleBarStyle = config.selectorStyle.getTitleBarStyle();
        if (StyleUtils.checkStyleValidity(titleBarStyle.getPreviewTitleBackgroundColor())) {
            setBackgroundColor(titleBarStyle.getPreviewTitleBackgroundColor());
        } else if (StyleUtils.checkSizeValidity(titleBarStyle.getTitleBackgroundColor())) {
            setBackgroundColor(titleBarStyle.getTitleBackgroundColor());
        }
        if (StyleUtils.checkStyleValidity(titleBarStyle.getPreviewTitleLeftBackResource())) {
            ivLeftBack.setImageResource(titleBarStyle.getPreviewTitleLeftBackResource());
        }
        rlAlbumBg.setOnClickListener(null);
        viewAlbumClickArea.setOnClickListener(null);
//        RelativeLayout.LayoutParams layoutParams = (LayoutParams) rlAlbumBg.getLayoutParams();
//        layoutParams.removeRule(RelativeLayout.END_OF);
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlAlbumBg.setBackgroundResource(R.drawable.ps_ic_trans_1px);
        ivArrow.setVisibility(GONE);
        viewAlbumClickArea.setVisibility(GONE);
    }

    @Override
    protected void init() {
        super.init();
        tvSelected = findViewById(R.id.ps_tv_selected);
        tvSelectedWord = findViewById(R.id.ps_tv_selected_word);
        tvSelectedWord.setOnClickListener(v -> {
            if(selectedClick != null) selectedClick.run();
        });
        tvSelected.setOnClickListener(v -> {
            if(selectedClick != null) selectedClick.run();
        });
    }
}
