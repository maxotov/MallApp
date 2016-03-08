package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import kz.itdamu.mallapp.R;


public class BaseActivity extends AppCompatActivity {
    public static final int SCREEN_MAIN = 0;
    public static final int SCREEN_LIKE = 1;
    public static final int SCREEN_VIEW = 2;
    public static final int SCREEN_FEEDBACK = 3;
    public static final int SCREEN_ABOUT = 4;
    protected Drawer drawer;
    private Toolbar toolbar;

    protected Drawer initDrawer(Toolbar toolbar) {
        this.toolbar = toolbar;
        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColorRes(android.R.color.white)
                .addDrawerItems(initDrawerItems())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        int identifier = iDrawerItem.getIdentifier();
                        Intent  intent;
                        switch (identifier) {
                            case SCREEN_MAIN:
                                //intent = new Intent(BaseActivity.this, SearchActivity.class);
                                Toast.makeText(BaseActivity.this, "Меню о приложении", Toast.LENGTH_SHORT).show();
                                // startActivity(intent);
                                return true;
                            case SCREEN_LIKE:
                                //intent = new Intent(BaseActivity.this, AddDrugActivity.class);
                                //startActivity(intent);
                                Toast.makeText(BaseActivity.this, "Меню о приложении", Toast.LENGTH_SHORT).show();
                                return true;
                            case SCREEN_VIEW:
                                //intent = new Intent(BaseActivity.this, BalanceActivity.class);
                                //startActivity(intent);
                                Toast.makeText(BaseActivity.this, "Меню о приложении", Toast.LENGTH_SHORT).show();
                                return true;
                            case SCREEN_FEEDBACK:
                                //intent = new Intent(BaseActivity.this, BalanceActivity.class);
                                //startActivity(intent);
                                Toast.makeText(BaseActivity.this, "Меню о приложении", Toast.LENGTH_SHORT).show();
                                return true;
                            case SCREEN_ABOUT:
                                Toast.makeText(BaseActivity.this, "Меню о приложении", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
        drawerBuilder.withHeader(R.layout.drawer_header);
        drawer = drawerBuilder.build();
        return drawer;
    }

    private IDrawerItem[] initDrawerItems() {
        List<IDrawerItem> items = new ArrayList<>();
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_main)
                .withIcon(R.drawable.ic_attachment)
                .withSelectedIcon(R.drawable.ic_attachment)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_MAIN));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_like)
                .withIcon(R.drawable.ic_image)
                .withSelectedIcon(R.drawable.ic_image)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_LIKE));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_view)
                .withIcon(R.drawable.ic_map_marker)
                .withSelectedIcon(R.drawable.ic_map_marker)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_VIEW));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_feedback)
                .withIcon(R.drawable.ic_emoticon)
                .withSelectedIcon(R.drawable.ic_emoticon)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_FEEDBACK));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_about)
                .withIcon(R.drawable.ic_emoticon)
                .withSelectedIcon(R.drawable.ic_emoticon)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_ABOUT));
        return items.toArray(new IDrawerItem[items.size()]);
    }

    protected void makeNavigationBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upIntent = NavUtils.getParentActivityIntent(BaseActivity.this);
                if (NavUtils.shouldUpRecreateTask(BaseActivity.this, upIntent)) {
                    TaskStackBuilder.create(BaseActivity.this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(BaseActivity.this, upIntent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    protected Drawer resetDrawer() {
        if (drawer == null) {
            return null;
        }
        drawer.removeAllItems();
        drawer.addItems(initDrawerItems());
        return drawer;
    }

}
