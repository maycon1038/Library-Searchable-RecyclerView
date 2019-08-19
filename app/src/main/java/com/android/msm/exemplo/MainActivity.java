package com.android.msm.exemplo;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.msm.recycleviewexpandable.AdapterUtil;
import com.android.msm.recycleviewexpandable.adapters.RecyclerAdapter;
import com.android.msm.recycleviewexpandable.interfaces.AdapterRecycleView;
import com.android.msm.recycleviewexpandable.interfaces.RecyclerViewOnCheckBox;
import com.android.msm.recycleviewexpandable.interfaces.RecyclerViewOnCircleProgressView;
import com.android.msm.recycleviewexpandable.interfaces.RecyclerViewOnClickListener;
import com.android.msm.recycleviewexpandable.interfaces.RecyclerViewOnListTextView;
import com.android.msm.recycleviewexpandable.interfaces.RecyclerViewOnRatingBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;

import static com.android.msm.recycleviewexpandable.Util.carregarCircleProgressView;


public class MainActivity extends AppCompatActivity implements AdapterRecycleView, RecyclerViewOnClickListener,
        RecyclerViewOnCheckBox, ActionMode.Callback, RecyclerViewOnRatingBar, RecyclerViewOnListTextView, RecyclerViewOnCircleProgressView {

    private static JsonArray jsonGroup = new JsonArray();
    animaisDAO dao = new animaisDAO(this);
    ActionMode mActionMode;
    private ArrayList<Integer> listID;
    private ArrayList<String> ItensDatabase;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private RecyclerAdapter myAdapter;
    private int checkedCount = 0;
    private Integer cProg;
    private Integer ImgVeiw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Cães");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (dao.buscarTudo() == null) {
            animaisVO vo = new animaisVO();
            vo.setRankig(1);
            vo.setEspecie("Cão");
            vo.setRaca("Border Collie");
            vo.setImg("https://www.infoescola.com/wp-content/uploads/2010/08/border-collie_593634296.jpg");
            dao.insert(vo);
            vo.setRankig(2);
            vo.setEspecie("Cão");
            vo.setRaca("Poodle");
            vo.setImg("http://portalmelhoresamigos.com.br/wp-content/uploads/2015/11/poodle_cachorro.png");
            dao.insert(vo);
            vo.setRankig(3);
            vo.setEspecie("Cão");
            vo.setRaca("Pastor Alemão");
            vo.setImg("https://tudosobrecachorros.com.br/wp-content/uploads/pastor-alemao-01.jpg");
            dao.insert(vo);
            vo.setRankig(4);
            vo.setEspecie("Cão");
            vo.setRaca("Golden Retriever");
            vo.setImg("https://img.quizur.com/f/img5cc8647699a3f3.43367462.jpg?lastEdited=1556636800");
            dao.insert(vo);
            vo.setRankig(5);
            vo.setEspecie("Cão");
            vo.setRaca("Doberman");
            vo.setImg("https://love.doghero.com.br/wp-content/uploads/2018/03/shutterstock_75891091-768x510.jpg");
            dao.insert(vo);
            vo.setRankig(6);
            vo.setEspecie("Cão");
            vo.setRaca("Pastro de Shetland");
            vo.setImg("https://upload.wikimedia.org/wikipedia/commons/6/62/ShetlandShpdogBlue2_wb.jpg");
            dao.insert(vo);
            vo.setRankig(7);
            vo.setEspecie("Cão");
            vo.setRaca("Labrador Retriever");
            vo.setImg("http://gatoecachorro.com/wp-content/uploads/2015/08/Labrador-Retriever.png");
            dao.insert(vo);
            vo.setRankig(8);
            vo.setEspecie("Cão");
            vo.setRaca("Papillon");
            vo.setImg("https://cdn.britannica.com/72/8172-050-B6BC0AC1.jpg");
            dao.insert(vo);
            vo.setRankig(9);
            vo.setEspecie("Cão");
            vo.setRaca("Rottveiler");
            vo.setImg("https://cdn.diferenca.com/imagens/rottweiler-alemao-cke.jpg");
            dao.insert(vo);
            vo.setRankig(1);
            vo.setEspecie("Cão");
            vo.setRaca("Austrian Cattle Dog");
            vo.setImg("https://vetsmart-parsefiles.s3.amazonaws.com/abd60f9c32a113fb7cfc09e6fc82963a_breed.jpg");
            dao.insert(vo);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listID = new ArrayList<>();
        ItensDatabase = new ArrayList<>();
        listID.add(R.id.tv_id);
        ItensDatabase.add("ranking");
        listID.add(R.id.tv_name);
        ItensDatabase.add("raca");

        cProg = R.id.circleView_img_obj;
        ImgVeiw = R.id.imageObj;


//        Intent it = getIntent();
//        if (it != null && it.getStringExtra(SearchManager.QUERY) != null) {
//            if (Intent.ACTION_SEARCH.equalsIgnoreCase(it.getAction())) {
//                String q = it.getStringExtra(SearchManager.QUERY);
//                SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
//                        SearchableProvider.AUTHORITY,
//                        SearchableProvider.MODE);
//                searchRecentSuggestions.saveRecentQuery(q, null);
//                Cursor cursor = dao.buscarStrings(q);
//                if (cursor != null && cursor.getCount() >= 1) {
//                    jsonGroup = getJsonGroup(cursor);
//                    AdapterUtil.with(this).configRecycleViewAdapter(R.layout.itens, listID, ItensDatabase, R.id.idCheckedView, cProg, ImgVeiw, R.id.ratingBar).
//                            setJson(jsonGroup).startRecycleViewAdapter(this);
//                } else {
//                    Toast.makeText(this, "nenhum dado encontrado", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        } else {
            if (jsonGroup.size() >= 1) {
                AdapterUtil.with(this).configRecycleViewAdapter(R.layout.itens, listID, ItensDatabase, R.id.idCheckedView, cProg, ImgVeiw, R.id.ratingBar).
                        setJson(jsonGroup).startRecycleViewAdapter(this);
            } else {
                Cursor cursor = dao.buscarTudo();
                if (cursor != null && cursor.getCount() >= 1) {
                    jsonGroup = getJsonGroup(cursor);
                    AdapterUtil.with(this).configRecycleViewAdapter(R.layout.itens, listID, ItensDatabase, R.id.idCheckedView, cProg, ImgVeiw,  R.id.ratingBar).
                            setJson(jsonGroup).startRecycleViewAdapter(this);
                } else {
                    Toast.makeText(this, "nenhum dado encontrado", Toast.LENGTH_SHORT).show();
                }

            }
      //  }

    }

    private JsonArray getJsonGroup(Cursor cursor) {

        JsonArray resultSet = new JsonArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JsonObject rowObject = new JsonObject();
            rowObject.addProperty("checked", false);
            rowObject.addProperty("ratingBar", 0.0);
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    String columnName = cursor.getColumnName(i);
                    String value = (cursor.getString(i) == null) ? "" : cursor.getString(i);
                    try {
                        rowObject.addProperty(columnName, value);
                    } catch (Exception e) {
                        Log.d(" cursorToJson ", e.getMessage());
                    }

                }
            }
            resultSet.add(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        return resultSet;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //     SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.action_searchable_activity);
        SearchView searchView = (SearchView) item.getActionView();
        //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //String q = it.getStringExtra(SearchManager.QUERY);
                SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(MainActivity.this,
                        SearchableProvider.AUTHORITY,
                        SearchableProvider.MODE);
                searchRecentSuggestions.saveRecentQuery(s, null);
                myAdapter.filterData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.filterData(s);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                myAdapter.filterData(null);
                return false;
            }
        });
        return true;
    }

    public void hendleSearch() {
        //    filter.initJson(this,convertlist( getListAnimais()));


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_delete) {
            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);
            searchRecentSuggestions.clearHistory();
            Toast.makeText(this, "Cookies removidos", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setRecyclerAdapter(RecyclerAdapter adapteRecycler) {
        myAdapter = adapteRecycler;
        mRecyclerView.setAdapter(myAdapter);
        myAdapter.setRecyclerViewOnClickListenerJson(this);
        myAdapter.setmRecyclerViewCheckBox(this);
        myAdapter.setmRecyclerViewRatingBar(this);
        myAdapter.setmRecyclerViewListTextView(this);
        myAdapter.setRecyclerViewProgressView(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hendleSearch();
        boolean consumed = (mActionMode != null);
        //  ImageView checkBox = (ImageView)view.findViewById(R.id.checkBox);
        if (consumed) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    public ArrayList<animais> getListAnimais() {
        ArrayList<animais> list = new ArrayList<>();
        animaisDAO dao = new animaisDAO(getBaseContext());
        Cursor cl = dao.buscarTudo();  // buscando o curso de todas lotacoes
        while (cl.moveToNext()) {
            list.add(
                    new animais(cl.getInt(1),
                            cl.getString(3)));   //calcula a distancia  e adiciona na lista

        }
        cl.close();

        return list;
    }

    @Override
    public void onClickListener(View view, JsonArray json, int position) {
        Log.d("JsonResultOnclick", json.get(position).getAsJsonObject().toString());

        if (mActionMode == null) {

            Toast.makeText(this, "Onclick...." +
                    json.get(position).getAsJsonObject().toString(), Toast.LENGTH_SHORT).show();
        } else {

            if (json.get(position).getAsJsonObject().get("checked").getAsBoolean()) {

                json.get(position).getAsJsonObject().addProperty("checked", false);
            } else {
                json.get(position).getAsJsonObject().addProperty("checked", true);

            }
            myAdapter.setFilterJsonArray(json);

        }
        jsonGroup = json;

    }

    @Override
    public void onLongPressClickListener(View view, JsonArray json, int position) {

        boolean consumed = (mActionMode == null);
        //  ImageView checkBox = (ImageView)view.findViewById(R.id.checkBox);
        if (consumed) {

            iniciarModoExclusao();
            json.get(position).getAsJsonObject().addProperty("checked", true);
            myAdapter.setFilterJsonArray(json);
            // lv.setItemChecked(position, true);
            //  atualizarItensMarcados(lv, position, checkBox);
        }
        jsonGroup = json;
    }

    private void iniciarModoExclusao() {
        mActionMode = startActionMode(this);
        //   updateChecked(position,view);
        mActionMode.setTitle("Selecionar Itens");


    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_delete_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        if (item.getItemId() == R.id.acao_delete) {


            for (int i = 0; i < jsonGroup.size(); i++) {
                Log.d(" checked ",  jsonGroup.get(i).getAsJsonObject().toString());
                while (jsonGroup.size() > 0 && jsonGroup.get(i).getAsJsonObject().get("checked").getAsBoolean()){
                    jsonGroup.remove(i);
                }
            }

            myAdapter.setFilterJsonArray(jsonGroup);

        } else if (item.getItemId() == R.id.acao_star) {

            for (int i = 0; i < jsonGroup.size(); i++) {
                if (jsonGroup.get(i).getAsJsonObject().get("checked").getAsBoolean()) {
                    if (jsonGroup.get(i).getAsJsonObject().get("ratingBar").getAsFloat() > 0) {
                        jsonGroup.get(i).getAsJsonObject().addProperty("ratingBar", 0.0);
                    } else {
                        jsonGroup.get(i).getAsJsonObject().addProperty("ratingBar", 1.0);
                    }
                }
            }
            myAdapter.setFilterJsonArray(jsonGroup);
        } else if (item.getItemId() == R.id.action_checkable) {
            if (item.isChecked()) {
                item.setChecked(false);
                for (int i = 0; i < jsonGroup.size(); i++) {
                    if (jsonGroup.get(i).getAsJsonObject().get("checked").getAsBoolean()) {
                        jsonGroup.get(i).getAsJsonObject().addProperty("checked", false);
                    }
                }

            } else {
                item.setChecked(true);
                for (int i = 0; i < jsonGroup.size(); i++) {
                    if (!jsonGroup.get(i).getAsJsonObject().get("checked").getAsBoolean()) {
                        jsonGroup.get(i).getAsJsonObject().addProperty("checked", true);
                    }
                }

            }
            myAdapter.setFilterJsonArray(jsonGroup);
        }

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        mActionMode = null;
        for (int i = 0; i < jsonGroup.size(); i++) {
            if (jsonGroup.get(i).getAsJsonObject().get("checked").getAsBoolean()) {
                jsonGroup.get(i).getAsJsonObject().addProperty("checked", false);
            }
        }
        checkedCount = 0;
        myAdapter.setFilterJsonArray(jsonGroup);

    }

    @Override
    public void OnCheckBox(CheckBox checkBox, JsonArray json, int position) {

        if (mActionMode != null) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(json.get(position).getAsJsonObject().get("checked").getAsBoolean());
            atualizarTitulo(json);
        } else {
            checkBox.setVisibility(View.GONE);
        }


    }

    private void atualizarTitulo(JsonArray json) {
        checkedCount = 0;
        for (int i = 0; i < json.size(); i++) {
            if (jsonGroup.get(i).getAsJsonObject().get("checked").getAsBoolean()) {
                checkedCount++;
            }

        }
        if (checkedCount > 0) {
            mActionMode.setTitle(String.valueOf(checkedCount));
        } else {
            mActionMode.setTitle("Selecionar Itens");

        }


    }

    @Override
    public void OnCheckBox(RatingBar ratingBar, JsonArray json, int position) {

        ratingBar.setRating(json.get(position).getAsJsonObject().get("ratingBar").getAsFloat());

    }

    @Override
    public void OnLisTextView(ArrayList<TextView> lisTextView, JsonArray json, int position) {

        lisTextView.get(0).setText(String.valueOf(position + 1));
        lisTextView.get(1).setText(json.get(position).getAsJsonObject().get("raca").getAsString());

    }

    @Override
    public void OnCircleProgressoView(CircleProgressView circleProgressView, final ImageView imageView, JsonArray json, int position) {
        final JsonObject jsonObject = json.get(position).getAsJsonObject();
        Log.d("ResultJson ", jsonObject.toString());

        if(jsonObject.has("img") ){
            final  CircleProgressView progressor = carregarCircleProgressView(circleProgressView);
            Ion.with(this).load(jsonObject.get("img").getAsString())
                    .progressHandler(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            double progress =  (100 * downloaded) / total;
                            progressor.setValue((int) progress);
                        }
                    })
                    .asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    progressor.setVisibility(View.GONE);
                    if(result != null) {
                        Log.d("ResultJson ", " Sucesso " +  jsonObject.get("img").getAsString());
                        Bitmap miniPhotoUserBitmap = Bitmap.createScaledBitmap(result, 300, 150, true);
                        imageView.setImageBitmap(miniPhotoUserBitmap);

                    }else{
                        Log.d("ResultJson ", " erro " +  jsonObject.get("img").getAsString());
                    }
                }

            });
        }



    }
}
