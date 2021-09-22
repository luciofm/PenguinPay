package mobi.largemind.penguinpay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import mobi.largemind.penguinpay.model.Country

class CountriesAdapter(context: Context, objects: List<Country>) :
    ArrayAdapter<Country>(context, R.layout.spinner_item, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return populateView(position, convertView, parent, R.layout.spinner_item)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return populateView(position, convertView, parent, R.layout.spinner_dropdown_item)
    }

    private fun populateView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
        layout: Int
    ): View {
        val country = getItem(position)!! //if index out of bounds it will throw, so assume non-null
        val view = convertView ?: LayoutInflater.from(context).inflate(layout, parent, false)

        view.findViewById<TextView>(R.id.country_name).text = country.country
        view.findViewById<TextView>(R.id.country_flag).text = country.flag
        view.findViewById<TextView>(R.id.country_code).text = country.phonePrefix

        return view
    }
}