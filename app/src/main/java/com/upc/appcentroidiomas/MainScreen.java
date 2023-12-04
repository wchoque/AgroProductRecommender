package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.databinding.ActivityMainScreenBinding;

public class MainScreen extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainScreenBinding binding;

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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_screen);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



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
        navController.navigate(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_screen);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}