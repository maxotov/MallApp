package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.entity.User;
import kz.itdamu.mallapp.helper.SQLiteHandler;
import kz.itdamu.mallapp.helper.SessionManager;


public class BaseActivity extends AppCompatActivity {
    public static final int SCREEN_MAIN = 0;
    public static final int SCREEN_LIKE = 1;
    public static final int SCREEN_FEEDBACK = 3;
    public static final int SCREEN_ABOUT = 4;
    public static final int SCREEN_LOGIN = 5;
    public static final int SCREEN_REGISTER = 6;
    public static final int SCREEN_MY_SHOPS = 7;
    public static final int SCREEN_LOGOUT = 8;
    protected Drawer drawer;
    protected AccountHeader accountHeader;
    private Toolbar toolbar;
    private SessionManager sessionManager;
    private User user;
    private SQLiteHandler db;
    private String IMG_URL = "http://itdamu.kz/MallBackend/images/icon/app_icon.png";

    protected Drawer initDrawer(Toolbar toolbar) {
        this.toolbar = toolbar;
        sessionManager = new SessionManager(this);
        db = new SQLiteHandler(getApplicationContext());
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
                                intent = new Intent(BaseActivity.this, MainActivity.class);
                                startActivity(intent);
                                return true;
                            case SCREEN_LIKE:
                                //intent = new Intent(BaseActivity.this, AddDrugActivity.class);
                                //startActivity(intent);
                                Toast.makeText(BaseActivity.this, "Меню о приложении", Toast.LENGTH_SHORT).show();
                                return true;
                            case SCREEN_LOGIN:
                                intent = new Intent(BaseActivity.this, LoginActivity.class);
                                startActivity(intent);
                                return true;
                            case SCREEN_REGISTER:
                                intent = new Intent(BaseActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                return true;
                            case SCREEN_LOGOUT:
                                sessionManager.setLogin(false);
                                db.deleteUsers();
                                intent = new Intent(BaseActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;
                            case SCREEN_MY_SHOPS:
                                intent = new Intent(BaseActivity.this, ManageShopActivity.class);
                                startActivity(intent);
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
        if(sessionManager.isLoggedIn()){
            user = db.getUserDetails();
            accountHeader = createAccountHeader();
            drawerBuilder.withAccountHeader(accountHeader);
        } else {
            drawerBuilder.withHeader(R.layout.drawer_header);
        }
        drawer = drawerBuilder.build();
        return drawer;
    }

    protected void initToolbar(Toolbar toolbar){
        this.toolbar = toolbar;
    }

    private AccountHeader createAccountHeader() {
        IProfile profile = new ProfileDrawerItem()
                .withName("Вы вошли как " + user.getName())
                .withEmail(user.getEmail())
                .withIcon(getResources().getDrawable(R.drawable.app_icon));

        return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(getResources().getDrawable(R.drawable.navigation_header))
                .addProfiles(profile)
                .withProfileImagesClickable(false)
                .withSelectionListEnabled(false)
                .build();
    }

    private IDrawerItem[] initDrawerItems() {
        List<IDrawerItem> items = new ArrayList<>();
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_main)
                .withIcon(R.drawable.ic_view_headline)
                .withSelectedIcon(R.drawable.ic_view_headline)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_MAIN));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_like)
                .withIcon(R.drawable.ic_star_outline)
                .withSelectedIcon(R.drawable.ic_star_outline)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_LIKE));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_feedback)
                .withIcon(R.drawable.ic_phone_in_talk)
                .withSelectedIcon(R.drawable.ic_phone_in_talk)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_FEEDBACK));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_about)
                .withIcon(R.drawable.ic_information_outline)
                .withSelectedIcon(R.drawable.ic_information_outline)
                .withTextColorRes(R.color.grey_700)
                .withSelectedTextColorRes(R.color.cyan_900)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_ABOUT));
        if(!sessionManager.isLoggedIn()){
            items.add(new PrimaryDrawerItem()
                    .withName(R.string.drawer_item_login)
                    .withIcon(R.drawable.ic_login_variant)
                    .withSelectedIcon(R.drawable.ic_login_variant)
                    .withTextColorRes(R.color.grey_700)
                    .withSelectedTextColorRes(R.color.cyan_900)
                    .withSelectedColor(android.R.color.transparent)
                    .withIdentifier(SCREEN_LOGIN));
            items.add(new PrimaryDrawerItem()
                    .withName(R.string.drawer_item_register)
                    .withIcon(R.drawable.ic_account_plus)
                    .withSelectedIcon(R.drawable.ic_account_plus)
                    .withTextColorRes(R.color.grey_700)
                    .withSelectedTextColorRes(R.color.cyan_900)
                    .withSelectedColor(android.R.color.transparent)
                    .withIdentifier(SCREEN_REGISTER));
        } else {
            items.add(new PrimaryDrawerItem()
                    .withName(R.string.drawer_item_my_shops)
                    .withIcon(R.drawable.ic_shopping)
                    .withSelectedIcon(R.drawable.ic_shopping)
                    .withTextColorRes(R.color.grey_700)
                    .withSelectedTextColorRes(R.color.cyan_900)
                    .withSelectedColor(android.R.color.transparent)
                    .withIdentifier(SCREEN_MY_SHOPS));
            items.add(new PrimaryDrawerItem()
                    .withName(R.string.drawer_item_logout)
                    .withIcon(R.drawable.ic_logout_variant)
                    .withSelectedIcon(R.drawable.ic_logout_variant)
                    .withTextColorRes(R.color.grey_700)
                    .withSelectedTextColorRes(R.color.cyan_900)
                    .withSelectedColor(android.R.color.transparent)
                    .withIdentifier(SCREEN_LOGOUT));
        }
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
