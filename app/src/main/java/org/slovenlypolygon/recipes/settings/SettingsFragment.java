package org.slovenlypolygon.recipes.settings;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.abstractfragments.SimpleCookItFragment;

public class SettingsFragment extends SimpleCookItFragment {
    private static final String HTML = "<!DOCTYPE html>\n" +
            "<html lang = \"ru\">\n" +
            "    <head>\n" +
            "        <title>Справка</title>\n" +
            "        <meta charset = \"utf-8\">\n" +
            "    </head>\n" +
            "\n" +
            "    <body>\n" +
            "        <h1>Гайд по использованию приложения</h1>\n" +
            "\n" +
            "        <br>\n" +
            "        <br>\n" +
            "\n" +
            "        <h2>Кратчайшее описание</h2>\n" +
            "        <p>Выбираете необходимые ингредиенты / категории блюд, переходите к списку рецептов, выбираете нужный,\n" +
            "            получаете пошаговое описание приготовления. Далее в справке — подробно про каждый раздел.</p>\n" +
            "\n" +
            "        <br>\n" +
            "        <br>\n" +
            "\n" +
            "        <h2>В боковом меню представлены основные разделы приложения:</h2>\n" +
            "        <ul>\n" +
            "            <li>Ингредиенты</li>\n" +
            "            <li>Категории</li>\n" +
            "            <li>Все рецепты</li>\n" +
            "            <li>Избранные блюда</li>\n" +
            "            <li>Холодильник</li>\n" +
            "            <li>Вам могут понравиться</li>\n" +
            "            <li>Списки покупок</li>\n" +
            "            <li>Сканировать чек</li>\n" +
            "        </ul>\n" +
            "\n" +
            "        <br>\n" +
            "        <br>\n" +
            "\n" +
            "        <h3>Ингредиенты</h3>\n" +
            "        <p>Здесь Вы можете отметить те ингредиенты, которые имеются у Вас в наличии.</p>\n" +
            "        <p>Нажимая на карточку, Вы выбираете или удаляете ингредиент из поиска.</p>\n" +
            "        <p>Сверху показаны выбранные компоненты в виде вкладок. По нажатии на вкладку Вы будете перемещены к\n" +
            "            соответствующей ей позиции в вертикальном списке. Нажатие на крестик удалит этот компонент.</p>\n" +
            "        <p>Справа сверху, в строке с названием приложения, присутствует лупа — Вы можете использовать поиск.</p>\n" +
            "        <p>После того, как Вы отметили все интересующие ингредиенты, нажмите на кнопку «К рецептам». Здесь можно увидеть\n" +
            "            блюда по запросу, отсортированные по релевантности.</p>\n" +
            "\n" +
            "        <br>\n" +
            "\n" +
            "        <h3>Категории</h3>\n" +
            "        <p>То же самое, что ингредиенты, но блюда сгруппированы по-другому. Алгоритм использования аналогичен\n" +
            "            ингредиентам</p>\n" +
            "\n" +
            "        <br>\n" +
            "\n" +
            "        <h3>Все рецепты</h3>\n" +
            "        <p>На этом экране выводятся все блюда, которые занесены в приложение. Можно найти любой интересующий рецепт,\n" +
            "            найти вдохновение или положить понравившееся блюдо в избранное. Об этом — далее</p>\n" +
            "\n" +
            "        <br>\n" +
            "\n" +
            "        <h3>Избранные блюда</h3>\n" +
            "        <p>Для того, чтобы отметить блюдо любимым (положить в избранное), перейдите на пошаговый рецепт. На картинке, в\n" +
            "            правом верхнем углу, будет значок сердечка. Нажмите на него</p>\n" +
            "        <p>Находясь на этом экране, блюдо можно удалить из избранных, свайпнув (проведя пальцем по блюду) влево или\n" +
            "            вправо.</p>\n" +
            "\n" +
            "        <br>\n" +
            "\n" +
            "        <h3>Холодильник</h3>\n" +
            "        <p>Холодильник — это избранные ингредиенты. Чтобы положить ингредиент в холодильник, долго подержите его на\n" +
            "            соответствующем экране, в появившемся диалоговом окне выберите «Добавить в избранное».</p>\n" +
            "        <p>Удаление ингредиента аналогично удалению любимого блюда: свайп вбок.</p>\n" +
            "\n" +
            "        <br>\n" +
            "\n" +
            "        <h3>Вам могут понравиться</h3>\n" +
            "        <p>Это — раздел с блюдами, подобранными под Ваш вкус.</p>\n" +
            "        <p>Для правильной работы рекомендаций положите несколько понравившихся блюд в избранное.\n" +
            "            Алгоритм перестроит список.</p>\n" +
            "\n" +
            "        <br>\n" +
            "\n" +
            "        <h3>Списки покупок</h3>\n" +
            "        <p>Чтобы добавить список, перейдите на интересующее Вас блюдо, нажмите на список ингредиентов (тот, что под\n" +
            "            большой картинкой). В появившемся диалоговом окне выберите соответствующий пункт меню. </p>\n" +
            "        <p>Удаление списка производится уже знакомым методом: свайп вбок.</p>\n" +
            "\n" +
            "        <br>\n" +
            "\n" +
            "        <h3>Сканировать чек</h3>\n" +
            "        <p>В данном разделе Вы можете сфотографировать (экспериментально!) или выбрать из галереи фото чека.</p>\n" +
            "        <p>Приложение распознает слова на нем и произведет поиск по ним как по ингредиентам. Далее Вы будете\n" +
            "            перенаправлены на экран выбора блюда.</p>\n" +
            "        <p>Стоит отметить, что для работы данной функции требуется подключение к интернету.</p>\n" +
            "    </body>\n" +
            "</html>";

    private SwitchMaterial switchMaterial;
    private CardView cardView;
    private Button button;
    private TextView helpText;
    private ConstraintLayout constraintLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        setRetainInstance(true);

        switchMaterial = rootView.findViewById(R.id.download_pictures_switch);
        cardView = rootView.findViewById(R.id.helpCard);
        button = rootView.findViewById(R.id.expandHelpButton);
        helpText = rootView.findViewById(R.id.helpText);
        constraintLayout = rootView.findViewById(R.id.expandableHelp);

        helpText.setText(Html.fromHtml(HTML, Html.FROM_HTML_MODE_COMPACT));

        button.setVisibility(View.VISIBLE);
        cardView.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(cardView, new AutoTransition());

            if (constraintLayout.getVisibility() == View.GONE) {
                constraintLayout.setVisibility(View.VISIBLE);
                button.setBackgroundResource(R.drawable.expandable_arrow_up);
            } else {
                constraintLayout.setVisibility(View.GONE);
                button.setBackgroundResource(R.drawable.expandable_arrow_down);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable @javax.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activity.getSharedPreferences("org.slovenlypolygon.recipes_preferences", Context.MODE_PRIVATE).edit().putBoolean("download_pictures", isChecked).apply();
            activity.notifySharedPreferencesChanged();
        });
    }
}

