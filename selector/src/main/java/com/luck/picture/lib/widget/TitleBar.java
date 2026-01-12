package com.luck.picture.lib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.luck.picture.lib.PictureSelectorFragment;
import com.luck.picture.lib.R;
import com.luck.picture.lib.bases.ScreenConst;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectorConfig;
import com.luck.picture.lib.config.SelectorProviders;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.TitleBarStyle;
import com.luck.picture.lib.utils.DensityUtil;
import com.luck.picture.lib.utils.StyleUtils;

/**
 * @author：luck
 * @date：2021/11/17 10:45 上午
 * @describe：TitleBar
 */
public class TitleBar extends RelativeLayout implements View.OnClickListener {

    protected RelativeLayout rlAlbumBg;
    protected ImageView ivLeftBack;
    protected ImageView ivArrow;
    protected MarqueeTextView tvTitle;
    protected TextView tvCancel;
    protected View titleBarLine;
    protected View viewAlbumClickArea;
    protected SelectorConfig config;
    protected View viewTopStatusBar;
    protected RelativeLayout titleBarLayout;

    private ImageView selectAllImage;

    private int mCurrentNum = 0;

    private boolean mIsAllSelected = false;

    private OnSelectAllListener mOnSelectAllListener;

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

        tvCancel.setOnClickListener(null);
        tvCancel.setVisibility(View.GONE);
    }

    public void resetToNoneSelect(String from) {
        if (mOnSelectAllListener == null) {
            return;
        }
        mIsAllSelected = false;
//            selectAllImage.contentDescription = context.getString(R.string.select_all)
        selectAllImage.setImageResource(R.drawable.ps_select_all);
        Log.d(PictureSelectorFragment.TAG, "allan💗 " + from + "reset to None Select");
    }

    public void setSelectedChange() {
        if (mOnSelectAllListener == null) {
            return;
        }
        var selectCount = config.getSelectCount();
        Log.d(PictureSelectorFragment.TAG, "allan💗 set Selected Change " + selectCount);
        mCurrentNum = selectCount;
        if (mIsAllSelected) {
            mIsAllSelected = false;
//            selectAllImage.contentDescription = context.getString(R.string.select_all)
            selectAllImage.setImageResource(R.drawable.ps_select_all);
        } else {
            mIsAllSelected = true;
            selectAllImage.setImageResource(R.drawable.ps_select_cancel);
        }
    }

    public TextView getTitleCancelView() {
        return tvCancel;
    }

    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        inflateLayout();
        setClickable(true);
        setFocusable(true);
        config = SelectorProviders.getInstance().getSelectorConfig();
        viewTopStatusBar = findViewById(R.id.top_status_bar);
        titleBarLayout = findViewById(R.id.rl_title_bar);
        ivLeftBack = findViewById(R.id.ps_iv_left_back);
        rlAlbumBg = findViewById(R.id.ps_rl_album_bg);
        viewAlbumClickArea = findViewById(R.id.ps_rl_album_click);
        tvTitle = findViewById(R.id.ps_tv_title);
        ivArrow = findViewById(R.id.ps_iv_arrow);
        tvCancel = findViewById(R.id.ps_tv_cancel);
        titleBarLine = findViewById(R.id.title_bar_line);
        ivLeftBack.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        rlAlbumBg.setOnClickListener(this);
        titleBarLayout.setOnClickListener(this);
        viewAlbumClickArea.setOnClickListener(this);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey));
        handleLayoutUI();
        if (TextUtils.isEmpty(config.defaultAlbumName)) {
            setTitle(config.chooseMode == SelectMimeType.ofAudio() ? getContext().getString(R.string.ps_all_audio) : getContext().getString(R.string.ps_camera_roll));
        } else {
            setTitle(config.defaultAlbumName);
        }
    }

    protected void inflateLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.ps_title_bar, this);
    }

    protected void handleLayoutUI() {

    }

    public ImageView getImageArrow() {
        return ivArrow;
    }

    /**
     * title bar line
     *
     * @return
     */
    public View getTitleBarLine() {
        return titleBarLine;
    }

    /**
     * Set title
     *
     * @param title
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * Get title text
     */
    public String getTitleText() {
        return tvTitle.getText().toString();
    }

    public void setTitleBarStyle() {
        if (config.isPreviewFullScreenMode) {
            ViewGroup.LayoutParams layoutParams = viewTopStatusBar.getLayoutParams();
            layoutParams.height = ScreenConst.instance.statusBarHeight;
        }
        PictureSelectorStyle selectorStyle = config.selectorStyle;
        TitleBarStyle titleBarStyle = selectorStyle.getTitleBarStyle();
        int titleBarHeight = titleBarStyle.getTitleBarHeight();
        if (StyleUtils.checkSizeValidity(titleBarHeight)) {
            titleBarLayout.getLayoutParams().height = titleBarHeight;
        } else {
            titleBarLayout.getLayoutParams().height = DensityUtil.dip2px(getContext(), 48);
        }

        if (titleBarLine != null) {
            if (titleBarStyle.isDisplayTitleBarLine()) {
                titleBarLine.setVisibility(VISIBLE);
                if (StyleUtils.checkStyleValidity(titleBarStyle.getTitleBarLineColor())) {
                    titleBarLine.setBackgroundColor(titleBarStyle.getTitleBarLineColor());
                }
            } else {
                titleBarLine.setVisibility(GONE);
            }
        }

        int backgroundColor = titleBarStyle.getTitleBackgroundColor();
        if (StyleUtils.checkStyleValidity(backgroundColor)) {
            setBackgroundColor(backgroundColor);
        }
        int backResId = titleBarStyle.getTitleLeftBackResource();
        if (StyleUtils.checkStyleValidity(backResId)) {
            ivLeftBack.setImageResource(backResId);
        }
        String titleDefaultText = StyleUtils.checkStyleValidity(titleBarStyle.getTitleDefaultTextResId())
                ? getContext().getString(titleBarStyle.getTitleDefaultTextResId()) : titleBarStyle.getTitleDefaultText();
        if (StyleUtils.checkTextValidity(titleDefaultText)) {
            tvTitle.setText(titleDefaultText);
        }
        int titleTextSize = titleBarStyle.getTitleTextSize();
        if (StyleUtils.checkSizeValidity(titleTextSize)) {
            tvTitle.setTextSize(titleTextSize);
        }
        int titleTextColor = titleBarStyle.getTitleTextColor();
        if (StyleUtils.checkStyleValidity(titleTextColor)) {
            tvTitle.setTextColor(titleTextColor);
        }
        if (config.isOnlySandboxDir) {
            ivArrow.setImageResource(R.drawable.ps_ic_trans_1px);
        } else {
            int arrowResId = titleBarStyle.getTitleDrawableRightResource();
            if (StyleUtils.checkStyleValidity(arrowResId)) {
                ivArrow.setImageResource(arrowResId);
            }
        }
        int albumBackgroundRes = titleBarStyle.getTitleAlbumBackgroundResource();
        if (StyleUtils.checkStyleValidity(albumBackgroundRes)) {
            rlAlbumBg.setBackgroundResource(albumBackgroundRes);
        }

        if (titleBarStyle.isHideCancelButton() || mOnSelectAllListener != null) {
            tvCancel.setVisibility(GONE);
        } else {
            tvCancel.setVisibility(VISIBLE);
            int titleCancelBackgroundResource = titleBarStyle.getTitleCancelBackgroundResource();
            if (StyleUtils.checkStyleValidity(titleCancelBackgroundResource)) {
                tvCancel.setBackgroundResource(titleCancelBackgroundResource);
            }
            String titleCancelText = StyleUtils.checkStyleValidity(titleBarStyle.getTitleCancelTextResId())
                    ? getContext().getString(titleBarStyle.getTitleCancelTextResId()) : titleBarStyle.getTitleCancelText();
            if (StyleUtils.checkTextValidity(titleCancelText)) {
                tvCancel.setText(titleCancelText);
            }
            int titleCancelTextColor = titleBarStyle.getTitleCancelTextColor();
            if (StyleUtils.checkStyleValidity(titleCancelTextColor)) {
                tvCancel.setTextColor(titleCancelTextColor);
            }
            int titleCancelTextSize = titleBarStyle.getTitleCancelTextSize();
            if (StyleUtils.checkSizeValidity(titleCancelTextSize)) {
                tvCancel.setTextSize(titleCancelTextSize);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ps_iv_left_back || id == R.id.ps_tv_cancel) {
            if (titleBarListener != null) {
                titleBarListener.onBackPressed();
            }
        } else if (id == R.id.ps_rl_album_bg || id == R.id.ps_rl_album_click) {
            if (titleBarListener != null) {
                titleBarListener.onShowAlbumPopWindow(this);
            }
        } else if (id == R.id.rl_title_bar) {
            if (titleBarListener != null) {
                titleBarListener.onTitleDoubleClick();
            }
        }
    }

    protected OnTitleBarListener titleBarListener;

    /**
     * TitleBar的功能事件回调
     *
     * @param listener
     */
    public void setOnTitleBarListener(OnTitleBarListener listener) {
        this.titleBarListener = listener;
    }

    public static class OnTitleBarListener {
        /**
         * 双击标题栏
         */
        public void onTitleDoubleClick() {

        }

        /**
         * 关闭页面
         */
        public void onBackPressed() {

        }

        /**
         * 显示专辑列表
         */
        public void onShowAlbumPopWindow(View anchor) {

        }
    }
}
