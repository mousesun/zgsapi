package com.zgs.api.appwidget.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @param <T1> header view holder
 * @param <T2> section header view holder
 * @param <T3> section content view holder
 * @param <T4> section footer view holder
 */
public abstract class MYListViewSectionAdapter<T1 extends MYDefaultViewHolder, T2 extends MYSectionViewHolder, T3 extends MYSectionViewHolder, T4 extends MYSectionViewHolder> extends MYDefaultAdapter<T1, MYDefaultViewHolder> {

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (type == MYViewHolderTypes.TYPE_CONTENT) {
            //获取内容的position
            position -= getHeaderCount();

            for (int n = 0; n < getSectionCount(); n++) {
                position -= getEachSectionHeaderCount(n);
                if (position < 0) {
                    return MYViewHolderTypes.TYPE_SECTION_HEAD;
                }

                position -= getEachSectionContentCount(n);
                if (position < 0) {
                    return MYViewHolderTypes.TYPE_SECTION_CONTENT;
                }

                position -= getEachSectionFooterCount(n);
                if (position < 0) {
                    return MYViewHolderTypes.TYPE_SECTION_FOOTER;
                }
            }
        }

        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MYViewHolderTypes.TYPE_SECTION_HEAD:
                return onCreateSectionHeaderHolder(parent);
            case MYViewHolderTypes.TYPE_SECTION_CONTENT:
                return onCreateSectionContentHolder(parent);
            case MYViewHolderTypes.TYPE_SECTION_FOOTER:
                return onCreateSectionFooterHolder(parent);
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    public int getContentCount() {
        int count = 0;

        for (int n = 0; n < getSectionCount(); n++) {
            count += getEachSectionHeaderCount(n);
            count += getEachSectionContentCount(n);
            count += getEachSectionFooterCount(n);
        }

        return count;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = super.getItemViewType(position);
        if (type >= MYViewHolderTypes.TYPE_CONTENT) {
            //获取内容的position
            position -= getHeaderCount();

            int temp;
            for (int n = 0; n < getSectionCount(); n++) {
                temp = getEachSectionHeaderCount(n);
                position -= temp;
                if (position < 0) {
                    ((T2) holder).section = n;
                    ((T2) holder).position = position + temp;
                    onBindSectionHeader((T2) holder);
                    return;
                }

                position -= getEachSectionContentCount(n);
                if (position < 0) {
                    ((T3) holder).section = n;
                    ((T3) holder).position = position + getEachSectionContentCount(n);
                    onBindSectionContent((T3) holder);
                    return;
                }

                position -= getEachSectionFooterCount(n);
                if (position < 0) {
                    ((T4) holder).section = n;
                    ((T4) holder).position = position + getEachSectionFooterCount(n);
                    onBindSectionFooter((T4) holder);
                    return;
                }
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public void onBindContent(MYDefaultViewHolder holder) {
    }

    public MYDefaultViewHolder onCreateContentHolder(ViewGroup parent) {
        return null;
    }

    public void onItemClick(View v, MYDefaultViewHolder holder) {
    }

    @Override
    public void onClick(View v) {
    }

    public abstract T1 onCreateHeaderHolder(ViewGroup parent);

    public abstract void onBindHeader(T1 holder);

    public abstract int getHeaderCount();

    public abstract int getSectionCount();

    public abstract int getEachSectionHeaderCount(int section);

    public abstract int getEachSectionContentCount(int section);

    public abstract int getEachSectionFooterCount(int section);

    public abstract T2 onCreateSectionHeaderHolder(ViewGroup parent);

    public abstract T3 onCreateSectionContentHolder(ViewGroup parent);

    public abstract T4 onCreateSectionFooterHolder(ViewGroup parent);

    public abstract void onBindSectionHeader(T2 holder);

    public abstract void onBindSectionContent(T3 holder);

    public abstract void onBindSectionFooter(T4 holder);

    public abstract void onLoadMore();
}
