package co.hjort.apps.compras.view;

import java.util.List;

/*
import com.android.vending.licensing.AESObfuscator;
import com.android.vending.licensing.LicenseChecker;
import com.android.vending.licensing.LicenseCheckerCallback;
import com.android.vending.licensing.ServerManagedPolicy;
*/

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
/*
import android.os.Handler;
import android.provider.Settings.Secure;
*/
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import co.hjort.apps.compras.domain.Produto;
import co.hjort.apps.compras.persistence.ProdutoDAO;

public class ListaCompras extends Activity {

	private static final int MENU_MARCAR_TODOS = 1;
	private static final int MENU_DESMARCAR_TODOS = 2;
	private static final int MENU_EXCLUIR_TODOS = 3;
	
	private Spinner spnSecao;
	private EditText txtProduto;
	private Button btnIncluir;
	
	private LinearLayout itens;
	private LayoutInflater inflater;
	
	private RemoverItemListener removerItemListener;
	private CheckItemListener checkItemListener;

	private ProdutoDAO dao;
	
	private int codigoSecao;
	private int qtdeProdutos;

	/*
    private static final String BASE64_PUBLIC_KEY = "...";

	private static final byte[] SALT = new byte[] { -46, 64, 30, -118, -103, -47, 74, -64, 41, 88, -94, -44, 77, -117, -36, -113, -11, 31, -64, 89 };

	private LicenseChecker mChecker;
	private LicenseCheckerCallback mLicenseCheckerCallback;
	
	// a handler on the UI thread
    private Handler mHandler;
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		spnSecao = (Spinner) findViewById(R.id.secao);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.secoes, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnSecao.setAdapter(adapter);
		spnSecao.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				popularItensSecao(pos + 1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		txtProduto = (EditText) findViewById(R.id.produto);
		btnIncluir = (Button) findViewById(R.id.incluir);
		btnIncluir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				incluirProduto();
			}
		});
		txtProduto.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					incluirProduto();
					return true;
				}
				return false;
			}
		});
		
		itens = (LinearLayout) findViewById(R.id.itens);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		removerItemListener = new RemoverItemListener();
		checkItemListener = new CheckItemListener();
		
		dao = ProdutoDAO.getInstance(this);
		
		/*
		mHandler = new Handler();
		
		final String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		
		// library calls this when it's done
		mLicenseCheckerCallback = new MyLicenseCheckerCallback();
		
		// construct the license checker with a policy
		mChecker = new LicenseChecker(this, new ServerManagedPolicy(this,
				new AESObfuscator(SALT, getPackageName(), deviceId)), BASE64_PUBLIC_KEY);
		
		// check the license
		doCheckLicense();
		*/
	}
	
	/*
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mChecker.onDestroy();
	}
	*/

	private void popularItensSecao(int secao) {
		codigoSecao = secao;
		qtdeProdutos = 0;
		
		itens.removeAllViews();
		
		List<Produto> lista = dao.buscarPorSecao(secao);
		for (Produto produto : lista) {
			View viewItem = inflater.inflate(R.layout.item, null);
			CheckBox chkProduto = (CheckBox) viewItem.findViewById(R.id.item01);
			Button btnRemover = (Button) viewItem.findViewById(R.id.remove01);
			int id = (int) produto.getId();
			
			viewItem.setId(id);
			
			chkProduto.setId(id);
			chkProduto.setText(produto.getNome());
			chkProduto.setChecked(produto.isMarcado());
			chkProduto.setOnCheckedChangeListener(checkItemListener);

			btnRemover.setId(id);
			btnRemover.setOnClickListener(removerItemListener);
			
			itens.addView(viewItem);
			qtdeProdutos++;
		}
		
		final String msg = getString(R.string.quantidade_produtos, qtdeProdutos);
		Toast.makeText(getApplication(), msg, Toast.LENGTH_LONG).show();
	}
	
	private class RemoverItemListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			int id = view.getId();
			if (dao.excluir(id)) {
				itens.removeView(itens.findViewById(id));
				Toast.makeText(getApplication(), R.string.produto_removido,
						Toast.LENGTH_SHORT).show();
				qtdeProdutos--;
			}
		}
	}
	
	private class CheckItemListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			dao.marcar(buttonView.getId(), isChecked);
		}
	}
	
	private void incluirProduto() {
		String nome = txtProduto.getText().toString();
		
		if (nome == null || "".equals(nome))
			return;
		
		View viewItem = inflater.inflate(R.layout.item, null);
		CheckBox chkProduto = (CheckBox) viewItem.findViewById(R.id.item01);
		Button btnRemover = (Button) viewItem.findViewById(R.id.remove01);
		
		int id = dao.incluir(codigoSecao, nome);

		viewItem.setId(id);
		
		chkProduto.setId(id);
		chkProduto.setText(nome);
		chkProduto.setOnCheckedChangeListener(checkItemListener);

		btnRemover.setId(id);
		btnRemover.setOnClickListener(removerItemListener);

		itens.addView(viewItem);
		
		Toast.makeText(getApplication(), R.string.incluido_produto, Toast.LENGTH_SHORT).show();
		txtProduto.setText("");
		txtProduto.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		// FIXME: readicionar esses itens do menu
//        menu.add(0, MENU_MARCAR_TODOS, 0, R.string.menu_marcar_todos);
//        menu.add(0, MENU_DESMARCAR_TODOS, 0, R.string.menu_desmarcar_todos);
        menu.add(0, MENU_EXCLUIR_TODOS, 0, R.string.menu_excluir_todos);
		
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_MARCAR_TODOS:
		case MENU_DESMARCAR_TODOS:
			boolean marcar = (item.getItemId() == MENU_MARCAR_TODOS);
			if (dao.marcarProdutos(codigoSecao, marcar)) {
				// FIXME: melhorar esse código de atualização da tela...
				List<Produto> lista = dao.buscarPorSecao(codigoSecao);
				for (Produto produto : lista) {
					int id = (int) produto.getId();
					View viewItem = itens.findViewById(id);
					// FIXME: como pegar esse checkbox??!
					CheckBox chkProduto = (CheckBox) viewItem.findViewById(id);
					// erro!
					chkProduto.setChecked(marcar);
				}
			}
			return true;
		case MENU_EXCLUIR_TODOS:
			if (dao.excluirProdutos(codigoSecao)) {
				itens.removeAllViews();
				Toast.makeText(getApplication(), R.string.produtos_removidos_secao,
						Toast.LENGTH_SHORT).show();
				qtdeProdutos = 0;
			}
			return true;
		}
		return false;
	}

	/*
    private void doCheckLicense() {
		mChecker.checkAccess(mLicenseCheckerCallback);
        setProgressBarIndeterminateVisibility(true);
        Toast.makeText(getApplication(), R.string.checking_license, Toast.LENGTH_LONG);
        mChecker.checkAccess(mLicenseCheckerCallback);
    }

	private class MyLicenseCheckerCallback implements LicenseCheckerCallback {

		@Override
		public void allow() {
			if (isFinishing()) {
                return;
            }
            displayResult(getString(R.string.allow));
		}

		@Override
		public void dontAllow() {
			if (isFinishing()) {
                return;
            }
			displayResult(getString(R.string.dont_allow));
			showDialog(0);
		}
		
		@Override
		public void applicationError(ApplicationErrorCode errorCode) {
			// TODO Auto-generated method stub
		}

	}
	
	private void displayResult(final String result) {
		mHandler.post(new Runnable() {
			public void run() {
		        Toast.makeText(getApplication(), result, Toast.LENGTH_LONG);
				setProgressBarIndeterminateVisibility(false);
			}
		});
	}
	*/
	
}