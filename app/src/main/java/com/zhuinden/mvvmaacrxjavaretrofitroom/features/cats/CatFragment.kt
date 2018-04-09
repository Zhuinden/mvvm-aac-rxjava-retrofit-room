package com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuinden.mvvmaacrxjavaretrofitroom.R
import com.zhuinden.mvvmaacrxjavaretrofitroom.application.CustomApplication
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.RecyclerViewScrollBottomOnSubscribe
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.createViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_cats.*

class CatFragment : Fragment() {
    private lateinit var catViewModel: CatViewModel

    private lateinit var catAdapter: CatAdapter

    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val application = context.applicationContext as CustomApplication
        catViewModel = createViewModel { CatViewModel(application.catRepository) }
        // if only *something* put together this ViewModel instance for me! :D
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_cats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(catRecyclerView) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = CatAdapter(catViewModel).also {
                catAdapter = it
            }
        }

        compositeDisposable += catViewModel.observeCats(onNext = { cats ->
            catAdapter.updateData(cats)
        })

        compositeDisposable += Observable.create(RecyclerViewScrollBottomOnSubscribe(catRecyclerView))
            .subscribeBy(onNext = { isScroll ->
                catViewModel.handleScrollToBottom(isScroll)
            })

        compositeDisposable += catViewModel.openCatEvent.subscribeBy(onNext = { cat ->
            val activity = requireActivity()
            activity.startActivity(Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(cat.sourceUrl);
            })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}