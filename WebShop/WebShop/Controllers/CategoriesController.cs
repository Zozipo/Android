using AutoMapper;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebShop.Data;
using WebShop.Data.Entitties;
using WebShop.Helpers;
using WebShop.Models;

namespace WebShop.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CategoriesController : ControllerBase
    {
        private readonly IMapper _mapper;
        private readonly AppEFContext _appEFContext;

        public CategoriesController(IMapper mapper, AppEFContext appEFContext)
        {
            _mapper = mapper;
            _appEFContext = appEFContext;
        }

        [HttpGet("list")]
        public async Task<IActionResult> Get()
        {
            Thread.Sleep(1000);
            var model = await _appEFContext.Categories
                .Where(x=>x.IsDeleted==false)
                .OrderBy(x=>x.Priority)
                .Select(x=>_mapper.Map<CategoryItemViewModel>(x))
                .ToListAsync();
            return Ok(model);
        }
        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var cat = await _appEFContext.Categories.FindAsync(id);
            if (cat == null)
                return NotFound();
            return Ok(_mapper.Map<CategoryItemViewModel>(cat));
        }
        [HttpPost("create")]
        public async Task<IActionResult> Create([FromBody] CategoryCreateItemVM model)
        {
            try
            {
                var category = _mapper.Map<CategoryEntity>(model);
                category.Image = ImageWorker.SaveImage(model.ImageBase64);
                await _appEFContext.Categories.AddAsync(category);
                await _appEFContext.SaveChangesAsync();
                return Ok(_mapper.Map<CategoryItemViewModel>(category));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
        [HttpPut("update")]
        public async Task<IActionResult> Update([FromBody] CategoryUpdateeItemVM model)
        {
            var cat = await _appEFContext.Categories.FindAsync(model.Id);
            if (cat == null)
                return NotFound();
            cat.Priority = model.Priority;
            cat.Name = model.Name;
            cat.Description = model.Description;
            if (!string.IsNullOrEmpty(model.ImageBase64))
            {
                ImageWorker.RemoveImage(cat.Image);
                cat.Image = ImageWorker.SaveImage(model.ImageBase64);
            }
            _appEFContext.Update(cat);
            await _appEFContext.SaveChangesAsync();
            return Ok();
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            var cat = await _appEFContext.Categories.FindAsync(id);
            if (cat == null)
                return NotFound();
            cat.IsDeleted = true;
            _appEFContext.SaveChanges();
            return Ok();
        }
    }
}
