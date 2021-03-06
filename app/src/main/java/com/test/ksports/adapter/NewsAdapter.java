package com.test.ksports.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.test.ksports.R;
import com.test.ksports.bean.NewsBean;
import com.test.ksports.util.SwitchPreferences;

import java.util.List;

/**
 * Created by kingwag on 2017/2/7.
 * 新闻适配器
 */

public class NewsAdapter extends BaseMultiItemQuickAdapter<NewsBean.DataBean.ArticlesBean> {
    private Context context;
    private List<NewsBean.DataBean.ArticlesBean> datas;
    public NewsAdapter(Context context,List<NewsBean.DataBean.ArticlesBean> data) {
        super(data);
        this.context = context;
        this.datas = data;
        addItemType(NewsBean.DataBean.ArticlesBean.TEXT, R.layout.news_item2);
        addItemType(NewsBean.DataBean.ArticlesBean.IMG, R.layout.news_item1);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NewsBean.DataBean.ArticlesBean articlesBean) {
        switch (baseViewHolder.getItemViewType()) {
            case NewsBean.DataBean.ArticlesBean.TEXT:
                baseViewHolder.setText(R.id.news_tittle2, articlesBean.getTitle())
                        .setText(R.id.news_auther2, articlesBean.getAuther_name())
                        .setText(R.id.news_date2,articlesBean.getList_dtime().substring(0,11))
                        .addOnClickListener(R.id.news_cross2)
                        .setVisible(R.id.news_like2, SwitchPreferences.getState(context, articlesBean.getWeburl() + "save"))
                        .setVisible(R.id.news_praise2, SwitchPreferences.getState(context, articlesBean.getWeburl() + "praise"));
                break;
            case NewsBean.DataBean.ArticlesBean.IMG:
                baseViewHolder.setText(R.id.news_tittle, articlesBean.getTitle())
                        .setText(R.id.news_auther, articlesBean.getAuther_name())
                        .setText(R.id.news_date,articlesBean.getList_dtime().substring(0,11))
                        .addOnClickListener(R.id.news_cross)
                        .setVisible(R.id.news_like, SwitchPreferences.getState(context, articlesBean.getWeburl() + "save"))
                        .setVisible(R.id.news_praise, SwitchPreferences.getState(context, articlesBean.getWeburl() + "praise"));

                if (!TextUtils.isEmpty(articlesBean.getThumbnail_pic())) {
                    Picasso.with(context).load(articlesBean.getThumbnail_pic()).into((ImageView) baseViewHolder.getView(R.id.news_img));
                }
                break;
        }


    }

    @Override
    public int getItemViewType(int position) {
        NewsBean.DataBean.ArticlesBean itemBean = datas.get(position);
        if (!TextUtils.isEmpty(itemBean.getThumbnail_pic())){
            return NewsBean.DataBean.ArticlesBean.IMG;
        }else {
            return NewsBean.DataBean.ArticlesBean.TEXT;
        }

    }
}
