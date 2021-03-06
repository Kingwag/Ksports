package com.test.ksports.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.test.ksports.R;
import com.test.ksports.adapter.FavAdapter;
import com.test.ksports.bean.NewsBean;
import com.test.ksports.db.DBManager;
import com.test.ksports.util.EmptyRecyclerView;
import com.test.ksports.util.StatusbarUtil;
import com.test.ksports.util.SwitchPreferences;

import java.util.List;

/**
 * 收藏页面
 */
public class FavoriteActivity extends BaseActivity {
    private EmptyRecyclerView favRecyclerView;//空布局
    private FavAdapter favAdapter;//列表适配器
    private RecyclerView.LayoutManager layoutManager;//列表管理
    private List<NewsBean.DataBean.ArticlesBean> datas;//数据集合
    private DBManager dbManager;//数据库管理
    private Context mContext;//上下文
    private RelativeLayout emptyLayout;//空布局
    private int selectedPotion;//点击位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusbarUtil.setStatusBarColor(this,getResources().getColor(R.color.red));
        setContentView(R.layout.activity_favorite);
        mContext = this;
        initDatabase();
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        favRecyclerView.scrollToPosition(favAdapter.getItemCount()-1);
    }


    /**
     * 初始化数据
     */
    private void initData() {

        datas= dbManager.getArticles(1);

    }


    /**
     * 初始化数据库
     */
    private void initDatabase(){
        dbManager = new DBManager(this);
    }

    /**
     * 初始化视图
     */
    private void initView( ) {
        //数据为空时显示的布局
        View emptyView =(RelativeLayout) findViewById(R.id.empty_include);
        favAdapter = new FavAdapter(this, datas);
        //设置列表动画
        favAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        favRecyclerView = (EmptyRecyclerView) findViewById(R.id.fav_recy);
        favRecyclerView.setAdapter(favAdapter);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        //设置空布局
        favRecyclerView.setEmptyView(emptyView);
        //item点击事件，点击进入详情页面
        favRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View itemview, int position) {
                selectedPotion = position;
                NewsBean.DataBean.ArticlesBean articlesBean = datas.get(position);
                Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
                intent.putExtra("item", articlesBean);
                intent.putExtra("position", selectedPotion);
                startActivityForResult(intent,100);
            }
        });
        favRecyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void SimpleOnItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定删除这条新闻？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NewsBean.DataBean.ArticlesBean articlesBean = datas.get(position);
                                dbManager.delete(articlesBean.getPk(),1);
                                favAdapter.remove(position);
                                SwitchPreferences.putState(mContext,articlesBean.getWeburl(),false);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==100 && resultCode==200){
            //刷新画面解决方案，通过获取取消收藏的位置，将其从集合删掉再调用适配器刷新画面的方法，同时解决多次返回和数据更新问题
            selectedPotion = data.getIntExtra("backPosition",0);
            datas.remove(selectedPotion);
            favAdapter.notifyItemRemoved(selectedPotion);
        }
    }
}
