package com.upc.appcentroidiomas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.databinding.ActivityMainScreenBinding;
import com.upc.appcentroidiomas.ui.login.LoginActivity;
import com.upc.appcentroidiomas.utils.AndroidUtil;

public class MainScreen extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainScreenBinding binding;
    TextView txtMainProfileDisplayName, txtMainProfileEmail;
    ImageView mainProfileAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainScreen.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_favoriteProduct,
                R.id.nav_profile,
                R.id.nav_manage_offer,
                R.id.nav_chat,
                R.id.nav_bank_account,
                R.id.nav_search_all_products,
                R.id.nav_update_request,
                R.id.nav_order,
                R.id.nav_order_history
        )
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_screen);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Access the header view and set the user details
        View headerView = navigationView.getHeaderView(0);
        txtMainProfileDisplayName = headerView.findViewById(R.id.txtMainProfileDisplayName);
        txtMainProfileEmail = headerView.findViewById(R.id.txtMainProfileEmail);
        mainProfileAvatar = headerView.findViewById(R.id.mainProfileAvatar);
        RatingBar ratingBar = headerView.findViewById(R.id.ratingBar);
        ratingBar.setRating(3.85f);

        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource(), getApplicationContext());
        LoggedInUser loggedUser = loginRepository.getLoggedUser();
        txtMainProfileDisplayName.setText(loggedUser.getDisplayName());
        txtMainProfileEmail.setText(loggedUser.getEmail());

        String imageUrl = loggedUser.getProfileImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            AndroidUtil.setProfilePic(getApplicationContext(), Uri.parse(imageUrl), mainProfileAvatar);
        }

        // Hide all menu items by default
        hideAllMenuItems(navigationView);

        // Enable menu items based on user type
        enableMenuItemsForUserType(navigationView, loggedUser);

        // Navigate to the appropriate fragment
        if (loggedUser.isAdmin()) {
            navController.navigate(R.id.nav_update_request);
        } else {
            navController.navigate(R.id.nav_home);
        }
        if (!loggedUser.isAccountEnabled()) {
            navController.navigate(R.id.nav_profile);
        }

/*

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                ProfileActivity profileFragment = new ProfileActivity();

                // Realiza la transacción de fragmentos
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main_screen, profileFragment)
                        .addToBackStack(null)  // Opcional, si quieres agregar esto al stack de navegación
                        .commit();
            }
            // Manejar otros elementos del menú si es necesario
            drawer.closeDrawer(GravityCompat.START);  // Cierra el drawer después de la selección
            return true;
        });

*/

/*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_screen);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                // Crea una instancia del nuevo fragment
                ProductsListActivity productsListFragment = new ProductsListActivity();

                // Realiza la transacción de fragmentos
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main_screen, productsListFragment)
                        .addToBackStack(null)  // Opcional, si quieres agregar esto al stack de navegación
                        .commit();
            }
            // Manejar otros elementos del menú si es necesario
            drawer.closeDrawer(GravityCompat.START);  // Cierra el drawer después de la selección
            return true;
        });
*/

        // Asegúrate de que nav_home sea el destino de inicio
        //navController.navigate(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource(), getApplicationContext());
            loginRepository.logout();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            //finish();

            Toast.makeText(this, "Logout exitoso!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_screen);
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();

    }

    private void hideAllMenuItems(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setVisible(false);
        }
    }

    private void enableMenuItemsForUserType(NavigationView navigationView, LoggedInUser loggedInUser) {
        Menu menu = navigationView.getMenu();
        if (loggedInUser.isAdmin()){
            // Show only the update request menu item for admin
            menu.findItem(R.id.nav_update_request).setVisible(true);
        }

        if (loggedInUser.isCompradorMayorista()){
            // always enabled
            menu.findItem(R.id.nav_profile).setVisible(true);

            // Show other menu items based on account enabled status
            menu.findItem(R.id.nav_home).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_favoriteProduct).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_chat).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_order_history).setVisible(loggedInUser.isAccountEnabled());
        }

        if (loggedInUser.isProductorAgricola()){
            // always enabled
            menu.findItem(R.id.nav_profile).setVisible(true);

            // Show other menu items based on account enabled status
            menu.findItem(R.id.nav_home).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_gallery).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_favoriteProduct).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_chat).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_manage_offer).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_bank_account).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_order).setVisible(loggedInUser.isAccountEnabled());
            menu.findItem(R.id.nav_order_history).setVisible(loggedInUser.isAccountEnabled());
        }
    }

    public void refreshLoggedUserAndOptions() {
        // Assuming you have a method in LoginRepository to refresh user data
        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource(), getApplicationContext());
        loginRepository.refreshLoggedUser(new LoginRepository.RefreshUserCallback() {
            @Override
            public void onRefresh(LoggedInUser updatedUser) {
                // Update the navigation header with new user details
                txtMainProfileDisplayName.setText(updatedUser.getDisplayName());
                txtMainProfileEmail.setText(updatedUser.getEmail());

                String imageUrl = updatedUser.getProfileImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    AndroidUtil.setProfilePic(getApplicationContext(), Uri.parse(imageUrl), mainProfileAvatar);
                }

                // Reset the menu options
                hideAllMenuItems(binding.navView);
                enableMenuItemsForUserType(binding.navView, updatedUser);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainScreen.this, "Failed to refresh user data", Toast.LENGTH_LONG).show();
            }
        });
    }

}