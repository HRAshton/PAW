package hrashton.ocpg;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;


public class HistoryActivity extends ListActivity implements AdapterView.OnItemLongClickListener {
    private ArrayAdapter<String> history;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;
    private long clearClickTime;
    private TreeSet<String> treeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = mSettings.edit();

        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        treeSet = new TreeSet(
                mSettings.getStringSet("History", new TreeSet<String>())
        );
        history = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>(treeSet)
        );

        this.setListAdapter(history);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

        history.remove(selectedItem);
        history.notifyDataSetChanged();
        treeSet.remove(selectedItem);
        editor.putStringSet("History", treeSet);
        editor.apply();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_history_clear) {
            if (clearClickTime + 10000 > System.currentTimeMillis()) {
                treeSet.clear();
                history.clear();
                history.notifyDataSetChanged();
                editor.remove("History");
                editor.apply();
            } else {
                Toast.makeText(this, R.string.toast_history_press_again_to_clear,
                        Toast.LENGTH_SHORT).show();
            }
            clearClickTime = System.currentTimeMillis();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
