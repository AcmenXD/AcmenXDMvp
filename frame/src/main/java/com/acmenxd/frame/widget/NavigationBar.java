package com.acmenxd.frame.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/5 16:20
 * @detail 底部导航栏, 支持红点显示
 */
public final class NavigationBar extends FrameLayout implements View.OnClickListener {
    public interface OnNavigationListener {
        void onTabChange(@IntRange(from = 0) int position);

        void onClick(@IntRange(from = 0) int position);
    }

    private Context mContext;
    private OnNavigationListener mListener;
    private List<View> mItems = new ArrayList<>();

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        addView(LayoutInflater.from(mContext).inflate(R.layout.widget_navigation_bar, null));
    }

    @Override
    public void onClick(View v) {
        if (mItems.contains(v)) {
            int position = mItems.indexOf(v);
            if (position >= 0) {
                changeTab(position);
                if (mListener != null) {
                    mListener.onClick(position);
                }
            }
        }
    }

    public void addItem(@NonNull String name, @DrawableRes int resId) {
        addItem(name, mContext.getResources().getDrawable(resId));
    }

    public void addItem(@NonNull String name, @NonNull Drawable drawable) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.navigation_bar_ll);
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.widget_navigation_bar_item, null);
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        itemView.setOnClickListener(this);
        layout.addView(itemView);
        TextView tvName = (TextView) itemView.findViewById(R.id.navigation_bar_item_tv_name);
        tvName.setText(name);
        tvName.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        mItems.add(itemView);
        setRedPoint(0, -1);
    }

    private void changeTab(@IntRange(from = 0) int position) {
        if (mItems.size() > position) {
            for (int i = 0, len = mItems.size(); i < len; i++) {
                View view = mItems.get(i);
                TextView tvName = (TextView) view.findViewById(R.id.navigation_bar_item_tv_name);
                if (i == position) {
                    view.setEnabled(false);
                    tvName.setSelected(true);
                } else {
                    view.setEnabled(true);
                    tvName.setSelected(false);
                }
            }
        }
    }

    public void setSelectTab(@IntRange(from = 0) int position) {
        changeTab(position);
        if (mListener != null) {
            mListener.onTabChange(position);
        }
    }

    public void setListener(@NonNull OnNavigationListener pListener) {
        this.mListener = pListener;
    }

    public void setRedPoint(@IntRange(from = 0) int position, @IntRange(from = -1) int number) {
        TextView tv = null;
        if (mItems.size() > position) {
            tv = (TextView) mItems.get(position).findViewById(R.id.navigation_bar_item_tv_red);
        }
        if (tv != null) {
            if (number < 0) {
                tv.setVisibility(INVISIBLE);
            } else if (number == 0) {
                tv.setText("");
                tv.setVisibility(VISIBLE);
            } else if (number > 99) {
                tv.setText("99");
                tv.setVisibility(VISIBLE);
            } else {
                tv.setText(String.valueOf(number));
                tv.setVisibility(VISIBLE);
            }
        }
    }
}
