//　共通処理

/*
const raceMap = {
    "にんげん": 1,
    "エルフ": 2,
    "ドワーフ": 3,
    "ノーム": 4,
    "ホビット": 5
};

const genderMap = {
    "おとこ": 1,
    "おんな": 2
};

const jobMap = {
    "せんし": 1,
    "りゅうきし": 2,
    "とうぞく": 3,
    "そうりょ": 4,
    "まじゅつし": 5,
    "さむらい": 6,
    "にんじゃ": 7,
    "ぎんゆうしじん": 8
};

let mode = "normal"; //通常表示
let selectedMember = null;//最初は誰も選ばれてない状態

let members = [];//最初は空配列 fetch後入る予定
let currentPage = 1;
const PAGE_SIZE = 15;


//ウィンドウ開閉
const openAddWindow = () => {
    openWindow("#addWindow");
        };

const closeAddWindow = () => {
    closeWindow("#addWindow");
        };

const openWindow = (id) => {//html側のidを取得
    document.querySelector(id).style.display = "block";
};

const closeWindow = (id) => {
    document.querySelector(id).style.display = "none";
};

const showError = (message) => {//html側のmessage取得

    document.querySelector("#errorMessage").innerText = message;

    openWindow("#errorWindow");
};

const closeError = () => {

    closeWindow("#errorWindow");
};

const clearAddForm = () => {//フォームを初期化する共通処理
    document.querySelector("#addName").value = "";
    document.querySelector("#addRace").selectedIndex = 0;//0番目（にんげん）
    document.querySelector("#addGender").selectedIndex = 0;//0番目（だんせい）
    document.querySelector("#addJob").selectedIndex = 0;//0番目（せんし）
};

const refresh = () => {//初めの画面に戻る共通処理
    selectedMember = null;
    mode = "normal";
    viewList();
};

const createFormData = (prefix) => {//formDataの作成共通化。updateとaddに対応
    const formData = new FormData();

    formData.append("name",
        document.querySelector(`#${prefix}Name`).value);
    formData.append("race",
        document.querySelector(`#${prefix}Race`).value);
    formData.append("gender",
        document.querySelector(`#${prefix}Gender`).value);
    formData.append("job",
        document.querySelector(`#${prefix}Job`).value);
    
    return formData;
};

const bindClick = (id, func) => {//クリックされた瞬間に実行
    document
        .querySelector(id)
        .addEventListener("click", func)
};

const validateName = (name) => {//入力エラー防止

   if (name.trim() === "") {
        showError("なまえ を にゅうりょくしてください");
        return false;
    }

    return true;
};

const drawTable = () => {//テーブル描画

    const tbody =
        document.querySelector("table.table tbody");

    tbody.innerHTML = "";//まずtrを空にする

    const start =
        (currentPage - 1) * PAGE_SIZE;

    const end =
        start + PAGE_SIZE;

    const pageMembers =
        members.slice(start, end);

    for(const member of pageMembers){

        const tr = document.createElement("tr");

        tr.addEventListener("click",
            event => trClick(event, member));

        tr.innerHTML = `
            <td></td>
            <td>${member.id}</td>
            <td>${member.name}</td>
            <td>${member.race}</td>
            <td>${member.gender}</td>
            <td>${member.job}</td>
        `;

        tbody.appendChild(tr);

    }
    updatePageInfo();

};

const nextPage = () => {

    const maxPage =
        Math.ceil(members.length / PAGE_SIZE);

    if(currentPage < maxPage){

        currentPage++;

        drawTable();

    }

};

const prevPage = () => {

    if(currentPage > 1){

        currentPage--;

        drawTable();

    }

};

const updatePageInfo = () => {

    const maxPage =
        Math.ceil(members.length / PAGE_SIZE);

    document.querySelector("#pageInfo").innerText =
        currentPage + " / " + maxPage;

};


const sortMembers = () => {//typeごとに昇順降順ソート

    const type =
        document.querySelector("#sortSelect").value;//htmlから取得したものをtypeへ
    members.sort((a, b) => {
        if (a[type] < b[type]) {
            return -1;//aを前にして
        }
        if (a[type] > b[type]) {
            return 1;//bを前にして
        }
        return 0;//順番はそのまま

    });
    currentPage = 1;//変更後１ページ目に戻る
    drawTable();//再描画
};

const bindChange = (id, func) => {//値変更があった瞬間に実行
    document
        .querySelector(id)
        .addEventListener("change", func);

};

//------------------------------------------------------------------------------------------------------------------------------------------

//一覧表示

const viewList = () => {
    fetch("/web3/guildmembers")
        .then(response => response.json())
        .then(response_data => {
            members = response_data.data;
            drawTable();
        });

};

//-----------------------------------------------------------------------------------------------------------------------------------------

//メンバーをみる（詳細）
const openViewWindow = () => {
    mode = "view"; // 閲覧モード
    openWindow("#viewWindow"); // 表示
};

const closeViewWindow = () => {
    mode = "normal";
    closeWindow("#viewWindow"); // 非表示
};

const openViewDetail = () => {
    closeViewWindow();
    document.querySelector("#viewName").innerText =
        selectedMember.name;
    document.querySelector("#viewRace").innerText =
        selectedMember.race;
    document.querySelector("#viewGender").innerText =
        selectedMember.gender;
    document.querySelector("#viewJob").innerText =
        selectedMember.job;
    openWindow("#viewDetailWindow");
};

const closeViewDetail = () => {
    mode = "normal";
    closeWindow("#viewDetailWindow");
};

//--------------------------------------------------------------------------------------------------------------------------------------------

// メンバー追加
const addMember = () => {

    const name = document.querySelector("#addName").value;
    if (!validateName(name)) {
        return;
    }

    const formData = createFormData("add");

    fetch("/web3/guildmembers", {// ServletへHTTPリクエストを送信
        method: "POST",// HTTPメソッドをPOSTにする（ServletのdoPost()が呼ばれる）
        body: formData// HTTPリクエストの本体。FormDataの内容(name,raceなど)をサーバへ送る
    })
        .then(response => response.json())// ResponseオブジェクトをJavaScriptオブジェクトへ変換
        .then(responseData => { // Servletから返ってきたJSONデータ
            viewList();// GETで一覧を取得し直してテーブルを更新

            // 入力フォームを初期状態へ戻す
            clearAddForm();
            // 追加ウィンドウを閉じる
            closeAddWindow();
        });
};

//------------------------------------------------------------------------------------------------------------------------------------------

//メンバー削除

const openDeleteWindow = () => { // ウィンドウが表示↓
    mode = "delete";//　削除モード
    document.querySelector("#deleteMessage").innerText ="どの なかまと わかれますか？";
    openWindow("#deleteWindow");
};

const closeDeleteWindow = () => {
   closeWindow("#deleteWindow");
};

const deleteMember = () => {
    fetch("/web3/guildmembers?id=" + selectedMember.id, {
    method: "DELETE"//HTTPメソッドのDELETE
    })
        .then(response => response.json())
        .then(responseData => {
            closeDeleteWindow();
            closeDeleteConfirm();
            refresh();            
        });
};

const openDeleteConfirm = () => {
    document.querySelector("#deleteConfirmMessage").innerText = selectedMember.name + " と わかれますか？";
    openWindow("#deleteConfirmWindow");
};

const closeDeleteConfirm = () => {
    closeWindow("#deleteConfirmWindow");
};

//----------------------------------------------------------------------------------------------------------------------------------------------

//変更 PUT

const openUpdateWindow = () => {
    mode = "update";//変更モード
    document.querySelector("#updateMessage").innerText ="どの なかまを へんこうしますか？";
    openWindow("#updateWindow");
  
}
const closeUpdateWindow = () => {
    mode = "normal";//通常モードへ
    closeWindow("#updateWindow");
};

const updateMember = () => {
        const name = document.querySelector("#updateName").value;

    if (!validateName(name))
    {
        return;
    }

    const formData = createFormData("update");
    formData.append("id", selectedMember.id);
   
    fetch(
          "/web3/guildmembers",
         {
            method: "PUT", body: formData
         } 
         )
        .then(response => response.json())
        .then(data => {
        closeUpdateForm();
            refresh();
        });
} 

const openUpdateForm = () => {
      document.querySelector("#updateName").value = selectedMember.name;//カーソル選択
      document.querySelector("#updateRace").value = raceMap[selectedMember.race];
      document.querySelector("#updateGender").value = genderMap[selectedMember.gender];
      document.querySelector("#updateJob").value = jobMap[selectedMember.job];
    openWindow("#updateFormWindow");
}

const closeUpdateForm = () => {
      mode = "normal";
    closeWindow("#updateFormWindow");
};

//-----------------------------------------------------------------------------------------------------------------------------------------

//イベント関連

const trClick = (event, member) => {

    const tr = event.currentTarget;

    if (tr.classList.contains("selected")) {//trにselected classはついているかの確認
        tr.classList.remove("selected");//選択中メンバーを解除
        selectedMember = null;
        return;
    }

    document
        .querySelectorAll("tbody tr")//html全体から tbody trを選択
        .forEach(row => row.classList.remove("selected"));//全部の行からselectedを消す
    tr.classList.add("selected");//クリックした行にCSSクラスselectedを付与


    selectedMember = member;//クリックされたメンバー情報を保存する

    if (mode === "delete") {// 削除モードの時
        closeDeleteWindow();//削除用ウィンドウを消す
        openDeleteConfirm();//削除肯定時のウィンドウを表示
    }
    if (mode === "update") {//変更モードの時
        closeUpdateWindow();//変更入り口ウィンドウの削除
        openUpdateForm();   //変更用フォームの表示
    }
    if (mode === "view") {//閲覧モードの時
    openViewDetail();//詳細閲覧用ウィンドウの表示
}
};

//-------------------------------------------------------------------------------------------------------------------------------------------------
        
// DOM関連
document.addEventListener('DOMContentLoaded', () => {
    viewList();

    bindClick("#openAddWindow", openAddWindow);
    bindClick("#closeButton", closeAddWindow);
    bindClick("#addButton", addMember);

    bindClick("#deleteButton", openDeleteWindow);
    bindClick("#deleteConfirmButton", deleteMember);
    bindClick("#deleteCancelButton", closeDeleteWindow);
    bindClick("#deleteNoButton", closeDeleteConfirm);

    bindClick("#openUpdateWindow", openUpdateWindow);
    bindClick("#updateButton", updateMember);
    bindClick("#updateCancelButton", closeUpdateWindow);
    bindClick("#updateFormCancelButton", closeUpdateForm);

    bindClick("#openViewWindow", openViewWindow);
    bindClick("#viewCancelButton", closeViewWindow);
    bindClick("#viewCloseButton", closeViewDetail);

    bindClick("#errorOkButton", closeError);

    bindClick("#prevButton", prevPage);

    bindClick("#nextButton", nextPage);

    bindChange("#sortSelect", sortMembers);
});

*/

const raceMap = {
  //Java側要求にあわせたかたち
  にんげん: 1,
  エルフ: 2,
  ドワーフ: 3,
  ノーム: 4,
  ホビット: 5,
};

const genderMap = {
  おとこ: 1,
  おんな: 2,
};

const jobMap = {
  せんし: 1,
  りゅうきし: 2,
  とうぞく: 3,
  そうりょ: 4,
  まじゅつし: 5,
  さむらい: 6,
  にんじゃ: 7,
  ぎんゆうしじん: 8,
};

let mode = "normal"; //通常表示
let selectedMember = null; //選択中メンバー
let members = []; //データ保持用
let grid = null; //Grid.jsインスタンス

//ウィンドウ開閉
const openWindow = (id) => {
  document.querySelector(id).style.display = "block";
};

const closeWindow = (id) => {
  document.querySelector(id).style.display = "none";
};

const openAddWindow = () => openWindow("#addWindow");
const closeAddWindow = () => closeWindow("#addWindow");
const showError = (message) => {
  document.querySelector("#errorMessage").innerText = message;
  openWindow("#errorWindow");
};

const closeError = () => closeWindow("#errorWindow");
const clearAddForm = () => {
  document.querySelector("#addName").value = "";
  document.querySelector("#addRace").selectedRace = 0;
  document.querySelector("#addGender").selectedGender = 0;
  document.querySelector("#addJob").selectedJob = 0;
};

const refresh = () => {
  selectedMember = null;
  mode = "normal";
  document
    .querySelectorAll("#gridWrapper tr")
    .forEach((row) => row.classList.remove("selected")); //選択行ハイライト(css)の解除
  viewList();
};

const createFormData = (character) => {
  const formData = new FormData();
  formData.append("name", document.querySelector(`#${character}Name`).value);
  formData.append("race", document.querySelector(`#${character}Race`).value);
  formData.append(
    "gender",
    document.querySelector(`#${character}Gender`).value
  );
  formData.append("job", document.querySelector(`#${character}Job`).value);
  return formData;
};

const click = (id, func) => {
  document.querySelector(id).addEventListener("click", func);
};

const availableName = (name) => {
  if (name.trim() === "") {
    showError("なまえ を にゅうりょくしてください");
    return false;
  }
  return true;
};

//Grid.js初期化
const initiateGrid = () => {
  grid = new gridjs.Grid({
    columns: [
      { id: "id", name: "ばんごう", sort: true },
      { id: "name", name: "なまえ", sort: true },
      { id: "race", name: "しゅぞく", sort: true },
      { id: "gender", name: "せいべつ", sort: true },
      { id: "job", name: "しょくぎょう", sort: true },
    ],
    data: [],
    pagination: {
      limit: 15,
      summary: true, //自動生成：全〇件中 〇〜〇件を表示
    },
    sort: true,
    language: {
      pagination: {
        previous: "← まえ",
        next: "つぎ →",
        showing: " ",
        results: () => "件",
      },
      noRecordsFound: "なかまが いません",
    },
  }).render(document.querySelector("#gridWrapper")); //html側のgridWrapperにテーブルdataを入れる

  document.querySelector("#gridWrapper").addEventListener("click", (event) => {
    //クリックイベント
    const tr = event.target.closest("tbody tr"); //クリック時一番近いtr
    const firstTd = tr.querySelector("td"); //td取得
    const memberId = firstTd.innerText.trim(); //↑で取得したテキスト取得→idの前後切り取り整形
    const member = members.find((m) => m.id == memberId); //↑で取得したidと同じidのものを配列内から探す
    const Selected = tr.classList.contains("selected"); //trに対しcssハイライトがあるか判定(t/f)

    document
      .querySelectorAll("#gridWrapper tr")
      .forEach((r) => r.classList.remove("selected")); //重複の防止

    if (Selected) {
      selectedMember = null; //すでに選択済みなら解除
    } else {
      tr.classList.add("selected"); //新規選択
      selectedMember = member;

      if (mode === "delete") {
        closeDeleteWindow();
        openDeleteConfirm();
      } else if (mode === "update") {
        closeUpdateWindow();
        openUpdateForm();
      } else if (mode === "view") {
        openViewDetail();
      }
    }
  });
};

//一覧取得-------------------------------------------------------------------------------------------------------------------------------------------
const viewList = () => {
  fetch("/web3/guildmembers")
    .then((response) => {
      return response.json();
    })
    .then((response_data) => {
      members = response_data.data;
      //Grid.jsのデータを更新して再描画
      grid
        .updateConfig({
          data: members,
        })
        .forceRender();
    });
};

//詳細閲覧-------------------------------------------------------------------------------------------------------------------------------------------------
const openViewWindow = () => {
  mode = "view";
  openWindow("#viewWindow");
};

const closeViewWindow = () => {
  mode = "normal";
  closeWindow("#viewWindow");
};

const openViewDetail = () => {
  closeViewWindow();
  document.querySelector("#viewName").innerText = selectedMember.name;
  document.querySelector("#viewRace").innerText = selectedMember.race;
  document.querySelector("#viewGender").innerText = selectedMember.gender;
  document.querySelector("#viewJob").innerText = selectedMember.job;
  openWindow("#viewDetailWindow");
};

const closeViewDetail = () => {
  mode = "normal";
  closeWindow("#viewDetailWindow");
};

//メンバー追加-------------------------------------------------------------------------------------------------------------------------------------------------
const addMember = () => {
  const name = document.querySelector("#addName").value;
  if (!availableName(name)) return;

  const formData = createFormData("add");

  fetch("/web3/guildmembers", {
    method: "POST",
    body: formData,
  })
    .then((response) => {
      return response.json();
    })
    .then(() => {
      clearAddForm();
      closeAddWindow();
      refresh();
    });
};

//メンバー削除----------------------------------------------------------------------------------------------------------------------------------------------------
const openDeleteWindow = () => {
  mode = "delete";
  document.querySelector("#deleteMessage").innerText =
    "どの なかまと わかれますか？";
  openWindow("#deleteWindow");
};

const closeDeleteWindow = () => closeWindow("#deleteWindow");

const deleteMember = () => {
  fetch("/web3/guildmembers?id=" + selectedMember.id, {
    method: "DELETE",
  })
    .then((response) => {
      return response.json();
    })
    .then(() => {
      closeDeleteWindow();
      closeDeleteConfirm();
      refresh();
    });
};

const openDeleteConfirm = () => {
  document.querySelector("#deleteConfirmMessage").innerText =
    selectedMember.name + " と わかれますか？";
  openWindow("#deleteConfirmWindow");
};

const closeDeleteConfirm = () => closeWindow("#deleteConfirmWindow");

//メンバー変更-----------------------------------------------------------------------------------------------------------------------------------------------------
const openUpdateWindow = () => {
  mode = "update";
  document.querySelector("#updateMessage").innerText =
    "どの なかまを へんこうしますか？";
  openWindow("#updateWindow");
};

const closeUpdateWindow = () => {
  mode = "normal";
  closeWindow("#updateWindow");
};

const updateMember = () => {
  const name = document.querySelector("#updateName").value;
  if (!availableName(name)) return;

  const formData = createFormData("update");
  formData.append("id", selectedMember.id);

  fetch("/web3/guildmembers", {
    method: "PUT",
    body: formData,
  }).then(() => {
    closeUpdateForm();
    refresh();
  });
};

const openUpdateForm = () => {
  document.querySelector("#updateName").value = selectedMember.name;
  document.querySelector("#updateRace").value = raceMap[selectedMember.race];
  document.querySelector("#updateGender").value =
    genderMap[selectedMember.gender];
  document.querySelector("#updateJob").value = jobMap[selectedMember.job];
  openWindow("#updateFormWindow");
};

const closeUpdateForm = () => {
  mode = "normal";
  closeWindow("#updateFormWindow");
};

//DOM読み込み完了時------------------------------------------------------------------------------------------------------------------------------------------
document.addEventListener("DOMContentLoaded", () => {
  initiateGrid();
  viewList();

  click("#openAddWindow", openAddWindow);
  click("#closeButton", closeAddWindow);
  click("#addButton", addMember);

  click("#deleteButton", openDeleteWindow);
  click("#deleteConfirmButton", deleteMember);
  click("#deleteCancelButton", closeDeleteWindow);
  click("#deleteNoButton", closeDeleteConfirm);

  click("#openUpdateWindow", openUpdateWindow);
  click("#updateButton", updateMember);
  click("#updateCancelButton", closeUpdateWindow);
  click("#updateFormCancelButton", closeUpdateForm);

  click("#openViewWindow", openViewWindow);
  click("#viewCancelButton", closeViewWindow);
  click("#viewCloseButton", closeViewDetail);

  click("#errorOkButton", closeError);
});
