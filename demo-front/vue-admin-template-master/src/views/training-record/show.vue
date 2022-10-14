<template>
  <div class="staff-container">
    <Row class="staff-searchBar">
      <Col span="5"></Col>
      <Col span="14">
        <div class="staff-title">培训记录详情</div>
        <Form>
          <FormItem label="培训老师">
            <Tag size="large">{{ pageData.trainingTeacher }}</Tag>
          </FormItem>

          <FormItem label="培训日期">
            <Tag size="large">{{ this.dateFormat(pageData.trainingDate) }}</Tag>
          </FormItem>

          <FormItem label="培训内容">
            <Tag size="large">{{pageData.trainingContent}}</Tag>
          </FormItem>

          <FormItem label="参与人员">
            <Table
              border
              size="small"
              :columns="displayTableColumns"
              :data="pageData.list"
            ></Table>
          </FormItem>

          <FormItem>
            <Button @click="back" size="large">返回</Button>
          </FormItem>
        </Form>
      </Col>
      <Col span="5"></Col>
    </Row>
  </div>
</template>
    
    <script>
import trainingRecordApi from "@/api/training-record";

export default {
  name: "StaffForm",
  data() {
    return {
      pageData: {
        id: "",
        trainingTeacher: "",
        trainingDate: new Date(),
        trainingContent: "",
        list: [],
      },
      displayTableColumns: [
        {
          type: "index",
          width: 55,
          align: "center",
        },
        {
          title: "姓名",
          key: "name",
        },
        {
          title: "性别",
          key: "sex",
          minWidth: 30,
          render: (h, params) => {
            const row = params.row;
            const text = row.sex === 1 ? "男" : row.sex === 2 ? "女" : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "身份证号码",
          key: "idcardNumber",
          minWidth: 80,
        },
        {
          title: "部门",
          key: "department",
          render: (h, params) => {
            const row = params.row;
            const text =
              row.department === 1
                ? "业务部"
                : row.department === 2
                ? "采购部"
                : row.stadepartmenttus === 3
                ? "行政部"
                : "未设置";
            return h("Tag", {}, text);
          },
        },
      ],
    };
  },
  created() {
    this.query();
  },
  methods: {
    query() {
      let formdata = { id: this.$route.params.id };
      trainingRecordApi.query(formdata).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.pageData = response.data.data;
        }
      });
    },
    dateFormat(rawDate) {
      // 设置时间格式
      const format = "YY年MM月DD日";
      // 获取单元格数据
      if (rawDate == null) {
        return null;
      }
      const date = new Date(rawDate);

      // 创建数组，如果数字小于 10，则在十位上填充 0
      const preArr = [
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
      ];

      const year = date.getFullYear();
      const month = date.getMonth() + 1; // 月份是从0开始的
      const day = date.getDate();

      const newTime = format
        .replace(/YY/g, year)
        .replace(/MM/g, preArr[month] || month)
        .replace(/DD/g, preArr[day] || day);
      return newTime;
    },
    back() {
      // 返回上一页，如果没有上一页返回首页
      if (window.history.length <= 1) {
        this.$router.push({ path: "/" });
        return false;
      } else {
        this.$router.go(-1);
      }
    },
  },
};
</script>
    
    <style lang="scss" scoped>
.staff {
  &-container {
    margin: 30px;
  }
  &-title {
    font-size: 30px;
    line-height: 46px;
    text-align: center;
    margin-bottom: 30px;
  }
}
</style>