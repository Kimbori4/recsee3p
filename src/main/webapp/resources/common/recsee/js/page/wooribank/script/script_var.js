var scriptList = new Map();
var scriptId = "";
var scriptwavesurfer;
var scriptJsonArray = new Object();
var sendScriptSocketClient;
var isRestartFlag; 
var scriptGrid;
var scriptStepEditGrid;
var searchScriptGrid;
var ui_padding_22;
var scriptTitle;
//스크립트 단계 클릭 시 index번호
var scriptStepFk;
var pagingToolbar;
var audioTag;
var insertDetailArray;
var scriptEvent;
var popUpScriptGrid;
var productListGroup;//상품 유형을 구뷴하기위함//단위형/추가형/트레이딩형
var ttsResult;
var playCount = 0;
var audioTag;
var scStepPK = 0;
var k = 0;
var realTimeValueArray;
var prizePk ;
var scriptType;
var productPk;
var pkarray=[];
var scriptGroupGrid;
var nowSortingColumn;
var allListenArray=[];
var productArray =[];
var productTypeArray =[];
var scriptChangeCodeArray=[];
var scriptChangeNameArray=[];
var changeText;
var searchGridLoaded = false;
var scriptVariableObject = [] ;
var productCode;
//scriptDetail내역을 받을 변수
var scriptDetailArray=[];
var resultData;
var typeselect;
var typeValue;
var typeText;
var writeCommonText="[공용문구]";
var incres = 0;
var scriptDetailData=[];

/* common script */
var scriptCommonGrid; // 공용 스크립트 팝업창 담는 변수
var selectedRowBeforeEdit; // 공용 스크립트 수정 취소 및 저장 시 상세 조회용 selectedId
var rowSelectDisabled; // 공용 스크립트 수정, 신규 작성 시 셀렉트 불가 eventId -- 저장 또는 취소 시 detach용
var commonEditType = ''; // 달력 팝업 전 editType 전달

/* transaction */
var transactionId = '';

/* 스크립트디테일 저장여부 확인 */
var newDetailSavedArray = [];

/* 상품 정보 json */
var productJson = '';

// 스크립트 그리드 클릭할때 수정모드인지 아닌지 구분 할 수 있는 값
var isEditMode = false;

/* 팝업창 : 수정, 변경이력 */
var editPopup = null;
var historyPopup = null;

/* 공용 디테일 추가 시 스크립트정보 임시 보관 - callbackSuccess 시에만 UI 추가용 */
var addCommonInfo = '';

/* 달력 저장 후 api 연동 */
var detailTransactionId = '';
var applyDate = '';
var applyType = '';

/* approveGrid*/
var scriptApproveListGrid = null;
var scriptApproveBeforeGrid = null;
var scriptApproveAfterGrid = null;
var approveGridLoaded = false;
var productName = ''; // 목록에서 상세 이동 시 대표상품명 표시
var applyName = ''; // 목록에서 상세 이동 시 날짜 표시 or 즉시적용 체크

/* 복사 팝업 */
var copyGridLoaded = false;

/* 복사 달력 팝업 */
var saveApplyDatePopup = null;

/* 도움말 팝업 */
var titleHelpMessage;
var headerHelpMessage;

/* 공용스크립트디테일 수정 시 원본 내용 */
var orgCommonData = [];

/* 스텝 클릭 시, 수정창에서 '수정' 표시 전 원본 스텝+디테일 저장 */
var clickedStepOrgDetail = [];
var clickedStepOrgName = null;

/* 수정 시작 시 스텝 개수 */
var editInitStepNum = 0;