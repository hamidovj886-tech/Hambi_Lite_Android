package uz.hambilite.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final int RED = Color.rgb(227, 6, 19);
    private final int DARK = Color.rgb(32, 35, 40);
    private final int MUTED = Color.rgb(105, 111, 120);
    private LinearLayout root, content, nav;
    private String currentPage = "Bosh sahifa";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(184, 0, 11));
        getWindow().setNavigationBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        buildShell();
        showHome();
    }

    private void buildShell() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(247, 248, 250));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(20), dp(18), dp(20), dp(18));
        header.setBackgroundColor(RED);
        header.setOrientation(LinearLayout.VERTICAL);
        TextView brand = new TextView(this);
        brand.setText("hambi lite");
        brand.setTextColor(Color.WHITE);
        brand.setTextSize(25);
        brand.setTypeface(null, Typeface.BOLD);
        TextView subtitle = new TextView(this);
        subtitle.setText("Beeline uchun yengil yordamchi");
        subtitle.setTextColor(Color.WHITE);
        subtitle.setTextSize(13);
        header.addView(brand);
        header.addView(subtitle);
        root.addView(header);

        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(16), dp(16), dp(18));
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        nav = new LinearLayout(this);
        nav.setPadding(dp(6), dp(7), dp(6), dp(7));
        nav.setBackgroundColor(Color.WHITE);
        String[] labels = {"Bosh sahifa", "Tariflar", "Paketlar", "Profil"};
        for (String label : labels) {
            TextView item = new TextView(this);
            item.setText(label);
            item.setTextSize(11);
            item.setGravity(Gravity.CENTER);
            item.setPadding(dp(3), dp(12), dp(3), dp(12));
            item.setTextColor(DARK);
            item.setOnClickListener(v -> navigate(label));
            nav.addView(item, new LinearLayout.LayoutParams(0, -2, 1));
        }
        root.addView(nav);
        setContentView(root);
    }

    private void navigate(String page) {
        currentPage = page;
        if (page.equals("Bosh sahifa")) showHome();
        else if (page.equals("Tariflar")) showTariffs();
        else if (page.equals("Paketlar")) showPackages();
        else showProfile();
    }

    private void reset(String title, String description) {
        content.removeAllViews();
        TextView heading = new TextView(this);
        heading.setText(title);
        heading.setTextSize(23);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setTextColor(DARK);
        content.addView(heading);
        TextView desc = new TextView(this);
        desc.setText(description);
        desc.setTextSize(14);
        desc.setTextColor(MUTED);
        desc.setPadding(0, dp(5), 0, dp(15));
        content.addView(desc);
    }

    private void showHome() {
        reset("Salom!", "Kerakli xizmatni tez va oson oching.");
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));
        card.setBackground(round(Color.WHITE, 18));
        TextView title = new TextView(this);
        title.setText("Balans va qoldiqlar");
        title.setTextColor(DARK);
        title.setTextSize(18);
        title.setTypeface(null, Typeface.BOLD);
        card.addView(title);
        TextView info = new TextView(this);
        info.setText("Balans va paket qoldiqlari Beeline tarmog‘ida tekshiriladi. Bu ilova ularni o‘zi soxta qiymat bilan ko‘rsatmaydi.");
        info.setTextColor(MUTED);
        info.setTextSize(13);
        info.setPadding(0, dp(8), 0, dp(12));
        card.addView(info);
        addButton(card, "Balansni tekshirish  •  *102#", () -> dialUssd("*102#"));
        addButton(card, "Internet qoldig‘i  •  *103#", () -> dialUssd("*103#"));
        addButton(card, "Daqiqalar  •  *106#", () -> dialUssd("*106#"));
        addButton(card, "SMS qoldig‘i  •  *105#", () -> dialUssd("*105#"));
        content.addView(card);

        addSectionTitle("Tezkor xizmatlar");
        addTile("Tariflar", "Tarif ma’lumotlarini ko‘rish", () -> showTariffs());
        addTile("Internet paketlari", "Paketlar va rasmiy ulash sahifasi", () -> showPackages());
        addTile("Yordam", "Beeline yordam sahifasini ochish", () -> openUrl("https://beeline.uz/uz/support/"));
        addNote("Eslatma: Hozirgi versiya mustaqil prototip. Beeline’ning ruxsatli API’si ulanmaguncha hisob ma’lumotlari avtomatik yuklanmaydi.");
    }

    private void showTariffs() {
        reset("Tariflar", "Tariflarning dolzarb narxlari va shartlarini rasmiy manbadan tekshiring.");
        addTile("Beeline tariflari", "Rasmiy tariflar ro‘yxati", () -> openUrl("https://beeline.uz/uz/products/tariffs"));
        addTile("Shaxsiy kabinet", "Mavjud bo‘lsa, hisobingizni boshqarish", () -> openUrl("https://beeline.uz/uz"));
        addNote("Tarifni almashtirishdan oldin narx, abonent to‘lovi va shartlarni tekshiring. Ushbu prototip tarifni o‘zi almashtirmaydi.");
    }

    private void showPackages() {
        reset("Internet paketlari", "Paketlar va ulash shartlari o‘zgarishi mumkin.");
        addTile("Internet paketlari", "Rasmiy paketlar sahifasini ochish", () -> openUrl("https://beeline.uz/uz"));
        addButton(content, "Internet qoldig‘ini tekshirish  •  *103#", () -> dialUssd("*103#"));
        addNote("Paket xarid qilish yoki to‘lov bajarish funksiyasi faqat rasmiy, xavfsiz integratsiya mavjud bo‘lganda qo‘shiladi.");
    }

    private void showProfile() {
        reset("Profil va yordam", "Shaxsiy ma’lumotlaringizni bu prototipga kiritishingiz shart emas.");
        addTile("Beeline yordam markazi", "Savol va murojaatlar", () -> openUrl("https://beeline.uz/uz/support/"));
        addTile("Beeline rasmiy sayti", "Tariflar va xizmatlar", () -> openUrl("https://beeline.uz/uz"));
        addTile("Ilova haqida", "Hambi Lite prototip • 1.0", () ->
            new AlertDialog.Builder(this).setTitle("Hambi Lite")
                .setMessage("Mustaqil prototip. Beeline yoki Hambi’ning rasmiy ilovasi emas. Rasmiy logotip va API’dan ruxsatsiz foydalanmaydi.")
                .setPositiveButton("Yopish", null).show());
    }

    private void addSectionTitle(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(18);
        t.setTypeface(null, Typeface.BOLD);
        t.setTextColor(DARK);
        t.setPadding(0, dp(20), 0, dp(10));
        content.addView(t);
    }

    private void addTile(String title, String detail, Runnable action) {
        LinearLayout tile = new LinearLayout(this);
        tile.setOrientation(LinearLayout.VERTICAL);
        tile.setPadding(dp(16), dp(14), dp(16), dp(14));
        tile.setBackground(round(Color.WHITE, 15));
        TextView t = new TextView(this);
        t.setText(title);
        t.setTextSize(16);
        t.setTypeface(null, Typeface.BOLD);
        t.setTextColor(DARK);
        TextView d = new TextView(this);
        d.setText(detail);
        d.setTextSize(13);
        d.setTextColor(MUTED);
        d.setPadding(0, dp(4), 0, 0);
        tile.addView(t);
        tile.addView(d);
        tile.setOnClickListener(v -> action.run());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = dp(10);
        content.addView(tile, lp);
    }

    private void addNote(String text) {
        TextView note = new TextView(this);
        note.setText(text);
        note.setTextColor(MUTED);
        note.setTextSize(12);
        note.setPadding(dp(3), dp(14), dp(3), dp(10));
        content.addView(note);
    }

    private void addButton(LinearLayout parent, String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(RED);
        b.setAllCaps(false);
        b.setTextSize(13);
        b.setOnClickListener(v -> action.run());
        parent.addView(b, new LinearLayout.LayoutParams(-1, -2));
    }

    private void dialUssd(String code) {
        try {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + Uri.encode(code)));
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "Telefon terish oynasini ochib bo‘lmadi.", Toast.LENGTH_LONG).show();
        }
    }

    private void openUrl(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Toast.makeText(this, "Sahifani ochib bo‘lmadi.", Toast.LENGTH_LONG).show();
        }
    }

    private GradientDrawable round(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private int dp(int value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
