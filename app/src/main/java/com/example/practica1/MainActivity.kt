package com.example.practica1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica1.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticleAdapter
    private val articleList = mutableListOf<Articles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mxBTN = findViewById(R.id.mxBTN) as Button
        val krBTN = findViewById(R.id.krBTN) as Button
        val jpBTN = findViewById(R.id.jpBTN) as Button

        mxBTN.setOnClickListener{setPais("mx")}
        krBTN.setOnClickListener{setPais("kr")}
        jpBTN.setOnClickListener{setPais("jp")}



        binding.searchNews.setOnQueryTextListener(this)

        initRecyclerView()
        searchNew("general")

    }

    private fun initRecyclerView(){

        adapter = ArticleAdapter(articleList)
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.adapter = adapter

    }

    private fun searchNew(category:String){

        val api = Retrofit2()

        CoroutineScope(Dispatchers.IO).launch {

            val call = api.getService()?.getNewsByCategory("us",category,"4b94054dbc6b4b3b9e50d8f62cde4f6c")
            val news: NewsResponse? = call?.body()

            runOnUiThread{

                if (call!!.isSuccessful){
                    if (news?.status.equals("ok")){
                        val articles = news?.articles ?: emptyList()
                        articleList.clear()
                        articleList.addAll(articles)
                        adapter.notifyDataSetChanged()
                    }else{
                        showMessage("Error en webservices")
                    }
                }else{
                    showMessage("Error en retrofit")
                }
                hideKeyBoard()
            }

        }

    }

    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun showMessage(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        showMessage(query.toString())
        if (!query.isNullOrEmpty()){
            searchNew(query.toLowerCase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }


    private fun setPais(Pais:String){

        val api = Retrofit2()

        CoroutineScope(Dispatchers.IO).launch {

            val call = api.getService()?.getNewsByCategory(Pais,"general","4b94054dbc6b4b3b9e50d8f62cde4f6c")
            val news: NewsResponse? = call?.body()

            runOnUiThread{

                if (call!!.isSuccessful){
                    if (news?.status.equals("ok")){
                        val articles = news?.articles ?: emptyList()
                        articleList.clear()
                        articleList.addAll(articles)
                        adapter.notifyDataSetChanged()
                    }else{
                        showMessage("Error en webservices")
                    }
                }else{
                    showMessage("Error en retrofit")
                }
                hideKeyBoard()
            }

        }

    }


}