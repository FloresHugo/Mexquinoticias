package mx.com.hugoflores.appnoticias;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Hugo Flores on 25/02/2017.
 */

public class Adapter extends BaseAdapter {

    private ArrayList<ModeloVista> arrayListItem;
    private Context context;
    private LayoutInflater layoutInflater;

    public Adapter(Context context, ArrayList<ModeloVista> arrayListItem) {
        this.context = context;
        this.arrayListItem = arrayListItem;
    }

    @Override
    public int getCount() {
        return arrayListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vistaItem = layoutInflater.inflate(R.layout.listp, parent,false);
        //ImageView imageView = (ImageView)vistaItem.findViewById(R.id.lpImv);
        TextView titulo = (TextView) vistaItem.findViewById(R.id.lpTvTitulo);
        TextView id = (TextView) vistaItem.findViewById(R.id.lpTvID);
        TextView fecha = (TextView) vistaItem.findViewById(R.id.lpTvFecha);
        TextView reportero = (TextView) vistaItem.findViewById(R.id.lpTvReportero);
        SmartImageView smartImageView = (SmartImageView) vistaItem.findViewById(R.id.lpImv);

        //imageView.setImageResource(arrayListItem.get(position).getImagen());
        Rect rect = new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());
        smartImageView.setImageUrl(arrayListItem.get(position).getImagen(),rect);
        titulo.setText(arrayListItem.get(position).getTitulo());
        fecha.setText(arrayListItem.get(position).getFecha());
        reportero.setText(arrayListItem.get(position).getReportero());
        id.setText(arrayListItem.get(position).getId());
        return vistaItem;
    }
}
