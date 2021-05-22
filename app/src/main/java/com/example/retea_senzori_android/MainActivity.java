package com.example.retea_senzori_android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.retea_senzori_android.authentication.service.AuthenticationService;
import com.example.retea_senzori_android.authentication.service.FirebaseAuthenticationService;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.example.retea_senzori_android.persistance.impl.FirebaseRepositoryImpl;
import com.example.retea_senzori_android.services.TestService;
import com.example.retea_senzori_android.services.impl.TestServiceImpl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

    private AuthenticationService authenticationService;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerServices();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        authenticationService.getLoggedUserData().subscribe(userData -> {
            if (userData == null) {
                navController.navigate(R.id.nav_login_fragment);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_logout) {
            authenticationService.logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void registerServices() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();

        authenticationService = new FirebaseAuthenticationService(new FirebaseRepositoryImpl<>());

        serviceLocator.register(TestService.class, new TestServiceImpl(new FirebaseRepositoryImpl<>()));
        serviceLocator.register(AuthenticationService.class, authenticationService);
    }
}