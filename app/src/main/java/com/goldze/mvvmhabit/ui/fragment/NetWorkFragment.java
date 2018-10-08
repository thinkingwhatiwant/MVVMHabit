package com.goldze.mvvmhabit.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.FragmentNetworkBinding;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.ui.vm.DemoViewModel;
import com.goldze.mvvmhabit.ui.vm.FormViewModel;
import com.goldze.mvvmhabit.ui.vm.NetWorkItemViewModel;
import com.goldze.mvvmhabit.ui.vm.NetWorkViewModel;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.ViewModelFactory;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 * 网络请求列表界面
 */

public class NetWorkFragment extends BaseFragment<FragmentNetworkBinding, NetWorkViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_network;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        //请求数据
        viewModel.requestNetWork(this);
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新
        viewModel.uc.onRefreshing.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //重新请求数据
                viewModel.requestNetWork(NetWorkFragment.this);
            }
        });
        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.uc.finishLoadmore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishLoadmore();
            }
        });
        //监听删除条目
        viewModel.deleteItemLiveData.observe(this, new Observer<NetWorkItemViewModel>() {
            @Override
            public void onChanged(@Nullable final NetWorkItemViewModel netWorkItemViewModel) {
                int index = viewModel.getPosition(netWorkItemViewModel);
                //删除选择对话框
                MaterialDialogUtils.showBasicDialog(getContext(), "提示", "是否删除【" + netWorkItemViewModel.entity.getName() + "】？ 列表索引值：" + index)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ToastUtils.showShort("取消");
                            }
                        }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        viewModel.deleteItem(netWorkItemViewModel);
                    }
                }).show();
            }
        });
    }
}
