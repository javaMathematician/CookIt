package org.slovenlypolygon.recipes.components.ingredients;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.components.AbstractComponentsFragment;
import org.slovenlypolygon.recipes.components.entitys.Component;
import org.slovenlypolygon.recipes.components.entitys.ComponentType;
import org.slovenlypolygon.recipes.utils.DeleteSubstrate;

public class FavoriteIngredientsFragment extends AbstractComponentsFragment {
    @Override
    protected ComponentType setDataSource() {
        return ComponentType.FAVORITE_COMPONENT;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DeleteSubstrate deleteSubstrate = new DeleteSubstrate(requireActivity(), position -> {
            Component component = componentAdapter.getComponents().get(position);
            component.setSelected(false);

            Toast.makeText(requireContext(), R.string.deleted_from_favorites, Toast.LENGTH_SHORT).show();
            onFavoriteComponentDeleted(component);
        });

        new ItemTouchHelper(deleteSubstrate.getItemTouchHelperCallback()).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        tabComponentAdapter.clearSelected();
        addDataSource();
    }

    @Override
    protected void onFavoriteComponentDeleted(Component component) {
        super.onFavoriteComponentDeleted(component);

        dao.deleteFavorite(component);
        componentAdapter.deleteComponent(component);
        tabComponentAdapter.updateComponent(component);

        checkQuantity();
    }

    @Override
    protected void updateData() {
        addDataSource();
    }

    @Override
    protected void checkQuantity() {
        textView.setText(componentAdapter.getItemCount() == 0 ? R.string.nothing_in_fridge : R.string.page_end);
    }
}
