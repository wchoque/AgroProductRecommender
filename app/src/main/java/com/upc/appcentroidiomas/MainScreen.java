package com.upc.appcentroidiomas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
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
                R.id.nav_chat,
                R.id.nav_bank_account,
                R.id.nav_search_all_products,
                R.id.nav_update_request)
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


        //TODO OCULTAR EN BASE AL TIPO DE USUARIO
        // Encuentra el ítem del menú que quieres ocultar
        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource(), getApplicationContext());
        LoggedInUser loggedUser = loginRepository.getLoggedUser();
        txtMainProfileDisplayName.setText(loggedUser.getDisplayName());
        txtMainProfileEmail.setText(loggedUser.getEmail());
        // Load the profile image
        String imageUrl = loggedUser.getProfileImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            //AndroidUtil.setProfilePic(getApplicationContext(), Uri.parse(imageUrl), mainProfileAvatar);

            Glide.with(this)
                    .load(imageUrl)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .apply(new RequestOptions().skipMemoryCache(true))
                    .apply(RequestOptions.circleCropTransform())
                    //.placeholder(R.drawable.placeholder_image) // Optional placeholder image
                    //.error(R.drawable.error_image) // Optional error image
                    .into(mainProfileAvatar);
        }

        int userType = loggedUser.getUserType();
        //1	Comprador Mayorista
        if (userType == 1) {
            // Establece la visibilidad del ítem (ocultar o mostrar)
            // Usa 'false' para ocultar, 'true' para mostrar
            MenuItem navHome = navigationView.getMenu().findItem(R.id.nav_home);
            navHome.setVisible(false);
        }

        //2	Productor Agricola
        if (userType == 2) {

        }

        //3	Admin
        if (userType == 3) {
            MenuItem navHome = navigationView.getMenu().findItem(R.id.nav_home);
            navHome.setVisible(false);

            MenuItem navGallery = navigationView.getMenu().findItem(R.id.nav_gallery);
            navGallery.setVisible(false);

            MenuItem navFavoriteProduct = navigationView.getMenu().findItem(R.id.nav_favoriteProduct);
            navFavoriteProduct.setVisible(false);

            MenuItem navProfile = navigationView.getMenu().findItem(R.id.nav_profile);
            navProfile.setVisible(false);

            //MenuItem navSearchAllProducts = navigationView.getMenu().findItem(R.id.nav_search_all_products);
            //navSearchAllProducts.setVisible(false);

            MenuItem navChat = navigationView.getMenu().findItem(R.id.nav_chat);
            navChat.setVisible(false);

            MenuItem navBankAccount = navigationView.getMenu().findItem(R.id.nav_bank_account);
            navBankAccount.setVisible(true);

            MenuItem navUpdateRequest = navigationView.getMenu().findItem(R.id.nav_update_request);
            navUpdateRequest.setVisible(true);
        }

        // Navegar al fragmento de chat si el usuario es admin
        if (userType == 3) {
            navController.navigate(R.id.nav_chat);
        } else {
            navController.navigate(R.id.nav_home);
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
}