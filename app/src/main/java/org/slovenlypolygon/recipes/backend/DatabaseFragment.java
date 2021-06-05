package org.slovenlypolygon.recipes.backend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.slovenlypolygon.recipes.backend.database.DataBaseHelper;
import org.slovenlypolygon.recipes.backend.database.DishComponentDAO;

public class DatabaseFragment extends Fragment {
    private DishComponentDAO dishComponentDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        dataBaseHelper.createDataBase();

        dishComponentDAO = new DishComponentDAO(dataBaseHelper.openDataBase());
    }

    public DishComponentDAO getDishComponentDAO() {
        return dishComponentDAO;
    }
}
